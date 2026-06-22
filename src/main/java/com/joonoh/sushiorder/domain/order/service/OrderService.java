package com.joonoh.sushiorder.domain.order.service;

import com.joonoh.sushiorder.domain.menu.entity.Menu;
import com.joonoh.sushiorder.domain.menu.exception.MenuNotFoundException;
import com.joonoh.sushiorder.domain.menu.repository.MenuRepository;
import com.joonoh.sushiorder.domain.order.dto.OrderItemRequest;
import com.joonoh.sushiorder.domain.order.dto.OrderResponse;
import com.joonoh.sushiorder.domain.order.dto.PlaceOrderRequest;
import com.joonoh.sushiorder.domain.order.entity.Order;
import com.joonoh.sushiorder.domain.order.entity.OrderStatus;
import com.joonoh.sushiorder.domain.order.exception.OrderNotFoundException;
import com.joonoh.sushiorder.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;

    /**
     * 주문 접수 (손님 → PENDING 상태로 저장)
     *
     * 이 시점에는 재고 차감 안 함. 직원이 confirm()해야 실제로 차감.
     * 같은 idempotencyKey로 재요청이 들어오면 기존 주문 그대로 반환.
     */
    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        // 1. Idempotency 체크 — 중복 요청이면 기존 주문 반환
        return orderRepository.findByIdempotencyKey(request.getIdempotencyKey())
                .map(existing -> {
                    log.info("중복 주문 요청 감지 — 기존 주문 반환. key={}, orderId={}",
                            request.getIdempotencyKey(), existing.getId());
                    return OrderResponse.from(existing);
                })
                .orElseGet(() -> createNewOrder(request));
    }

    private OrderResponse createNewOrder(PlaceOrderRequest request) {
        Order order = Order.builder()
                .tableId(request.getTableId())
                .sessionId(request.getSessionId())
                .idempotencyKey(request.getIdempotencyKey())
                .build();

        // 메뉴 정보를 가져와서 스냅샷으로 OrderItem에 박아둠
        for (OrderItemRequest itemReq : request.getItems()) {
            Menu menu = menuRepository.findById(itemReq.getMenuId())
                    .orElseThrow(() -> new MenuNotFoundException(itemReq.getMenuId()));

            if (!menu.isOrderable(itemReq.getQuantity())) {
                throw new IllegalStateException(
                        String.format("주문할 수 없는 메뉴입니다. 메뉴: %s", menu.getName()));
            }

            order.addItem(
                    menu.getId(),
                    menu.getName(),
                    menu.getPrice(),
                    itemReq.getQuantity()
            );
        }

        Order saved = orderRepository.save(order);
        log.info("새 주문 생성 — orderId={}, tableId={}, total={}",
                saved.getId(), saved.getTableId(), saved.getTotalPrice());

        return OrderResponse.from(saved);
    }

    /**
     * 주문 확정 (직원 → CONFIRMED 상태로 전환)
     *
     * 이 시점에 재고 차감 발생. 동시에 두 주문이 같은 메뉴를 차감하려 하면
     * Menu의 @Version 낙관적 락이 발동 → ObjectOptimisticLockingFailureException 발생.
     */
    @Transactional
    public OrderResponse confirmOrder(Long orderId) {
        Order order = findOrderOrThrow(orderId);

        // 1. 주문 상태 전이
        order.confirm();

        // 2. 재고 차감
        for (var item : order.getItems()) {
            Menu menu = menuRepository.findById(item.getMenuId())
                    .orElseThrow(() -> new MenuNotFoundException(item.getMenuId()));

            menu.decreaseStock(item.getQuantity());
            // 영속 상태 엔티티라 dirty checking으로 UPDATE 자동 발생
            // 만약 다른 트랜잭션이 동시에 같은 메뉴를 차감했다면
            // 커밋 시점에 @Version 충돌 → 예외 발생
        }

        log.info("주문 확정 — orderId={}", orderId);
        return OrderResponse.from(order);
    }

    /**
     * 주문 완료 (서빙됨)
     */
    @Transactional
    public OrderResponse completeOrder(Long orderId) {
        Order order = findOrderOrThrow(orderId);
        order.complete();
        return OrderResponse.from(order);
    }

    /**
     * 주문 취소
     * CONFIRMED 상태였다면 재고 복구도 같이 수행.
     */
    @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        Order order = findOrderOrThrow(orderId);
        boolean wasConfirmed = order.getStatus() == com.joonoh.sushiorder.domain.order.entity.OrderStatus.CONFIRMED;

        order.cancel();

        if (wasConfirmed) {
            for (var item : order.getItems()) {
                Menu menu = menuRepository.findById(item.getMenuId())
                        .orElseThrow(() -> new MenuNotFoundException(item.getMenuId()));
                menu.restoreStock(item.getQuantity());
            }
            log.info("CONFIRMED 주문 취소 — 재고 복구 완료. orderId={}", orderId);
        }

        return OrderResponse.from(order);
    }

    // ===== 조회 메서드 =====

    public OrderResponse getOrder(Long orderId) {
        return OrderResponse.from(findOrderOrThrow(orderId));
    }

    public List<OrderResponse> getActiveOrdersByTableId(Long tableId) {
        return orderRepository.findActiveOrdersByTableId(tableId).stream()
                .map(OrderResponse::from)
                .toList();
    }

    public List<OrderResponse> getOrdersBySessionId(Long sessionId) {
        return orderRepository.findBySessionId(sessionId).stream()
                .map(OrderResponse::from)
                .toList();
    }

    private Order findOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(OrderResponse::from)
                .toList();
    }
}