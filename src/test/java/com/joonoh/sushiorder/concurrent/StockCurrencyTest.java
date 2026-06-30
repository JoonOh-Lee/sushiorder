package com.joonoh.sushiorder.concurrent;

import com.joonoh.sushiorder.domain.menu.entity.Menu;
import com.joonoh.sushiorder.domain.menu.entity.MenuCategory;
import com.joonoh.sushiorder.domain.menu.repository.MenuRepository;
import com.joonoh.sushiorder.domain.order.dto.OrderItemRequest;
import com.joonoh.sushiorder.domain.order.dto.PlaceOrderRequest;
import com.joonoh.sushiorder.domain.order.entity.OrderStatus;
import com.joonoh.sushiorder.domain.order.repository.OrderRepository;
import com.joonoh.sushiorder.domain.order.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StockConcurrencyTest {

    @Autowired private OrderService orderService;
    @Autowired private MenuRepository menuRepository;
    @Autowired private OrderRepository orderRepository;

    private Long menuId;

    @BeforeEach
    void setUp() {
        Menu menu = Menu.builder()
                .name("동시성 테스트용 우니초밥")
                .description("재고 5")
                .price(8000)
                .category(MenuCategory.PREMIUM_SUSHI)
                .stockCount(5)
                .stationId(1L)
                .build();
        menuId = menuRepository.saveAndFlush(menu).getId();
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll(); // 테스트로 만든 주문 정리 (OrderItem cascade로 같이 삭제)
        menuRepository.deleteById(menuId); // 테스트로 만든 메뉴만 정리
    }
    @Test
    @DisplayName("재고 5개에 10명이 동시에 1개씩 주문하면, 정확히 5명만 성공해야 한다")
    void concurrentStockDeduction() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final int userIdx = i;
            executor.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await();

                    PlaceOrderRequest req = new PlaceOrderRequest();
                    setField(req, "idempotencyKey", UUID.randomUUID().toString());

                    OrderItemRequest item = new OrderItemRequest();
                    setField(item, "menuId", menuId);
                    setField(item, "quantity", 1);
                    setField(req, "items", List.of(item));

                    var orderResponse = orderService.placeOrder((long) userIdx, (long) userIdx, req);
                    orderService.confirmOrder(orderResponse.getId());
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    System.out.println("실패: " + e.getClass().getSimpleName()
                            + " - " + e.getMessage());
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        doneLatch.await();

        Menu finalMenu = menuRepository.findById(menuId).orElseThrow();

        long pendingCount = orderRepository.findAll().stream()
                .filter(o -> o.getStatus() == OrderStatus.PENDING)
                .count();
        long cancelledCount = orderRepository.findAll().stream()
                .filter(o -> o.getStatus() == OrderStatus.CANCELLED)
                .count();

        System.out.println("=================================");
        System.out.println("성공: " + successCount.get());
        System.out.println("실패: " + failCount.get());
        System.out.println("최종 재고: " + finalMenu.getStockCount());
        System.out.println("PENDING으로 남은 주문: " + pendingCount);
        System.out.println("CANCELLED 처리된 주문: " + cancelledCount);
        System.out.println("=================================");

        assertThat(successCount.get()).isEqualTo(5);
        assertThat(finalMenu.getStockCount()).isEqualTo(0);
        assertThat(pendingCount).isEqualTo(0);
        assertThat(cancelledCount).isEqualTo(5);

        executor.shutdown();
    }

    private void setField(Object target, String name, Object value) {
        try {
            var field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}