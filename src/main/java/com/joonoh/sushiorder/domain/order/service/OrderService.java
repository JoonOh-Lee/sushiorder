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
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
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
    @Retryable(
            retryFor = {
                    OptimisticLockingFailureException.class,
                    org.springframework.orm.jpa.JpaSystemException.class   // MariaDB 1020 에러도 잡힘
            },
            maxAttempts = 5,
            backoff = @Backoff(delay = 50, multiplier = 1.5)
    )
    public OrderResponse confirmOrder(Long orderId) {
        Order order = findOrderOrThrow(orderId);
        order.confirm();

        for (var item : order.getItems()) {
            Menu menu = menuRepository.findById(item.getMenuId())
                    .orElseThrow(() -> new MenuNotFoundException(item.getMenuId()));
            menu.decreaseStock(item.getQuantity());
        }

        log.info("주문 확정 — orderId={}", orderId);
        return OrderResponse.from(order);
    }

    @Recover
    @Transactional(noRollbackFor = IllegalStateException.class)
    public OrderResponse recoverFromConcurrencyFailure(Exception e, Long orderId) {
        log.error("재시도 한계 초과 — orderId={}, cause={}", orderId, e.getMessage());

        // 롤백되어 PENDING 상태로 남아있는 주문을 CANCELLED로 정리
        orderRepository.findById(orderId).ifPresent(order -> {
            if (order.getStatus() == OrderStatus.PENDING) {
                order.cancel();
                log.info("재시도 실패 후 주문 자동 취소 — orderId={}", orderId);
            }
        });

        throw new IllegalStateException("주문 처리에 실패했습니다. 잠시 후 다시 시도해주세요.");
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