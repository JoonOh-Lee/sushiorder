package com.joonoh.sushiorder.domain.order.entity;

import com.joonoh.sushiorder.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders") // 'order'는 SQL 예약어라 복수형 사용
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_id", nullable = false)
    private Long tableId;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @Column(name = "idempotency_key", nullable = false, unique = true, length = 100)
    private String idempotencyKey;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Version
    private Long version;

    @Builder
    private Order(Long tableId, Long sessionId, String idempotencyKey) {
        this.tableId = tableId;
        this.sessionId = sessionId;
        this.idempotencyKey = idempotencyKey;
        this.status = OrderStatus.PENDING;
        this.totalPrice = 0;
    }

    // ===== 외부 접근 제어 =====

    /** items는 외부에서 직접 수정 못 하게 unmodifiable로 노출 */
    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    // ===== 비즈니스 로직 =====

    /**
     * 주문에 메뉴 추가.
     * PENDING 상태에서만 가능.
     */
    public void addItem(Long menuId, String menuName, int unitPrice, int quantity) {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("확정/취소된 주문은 수정할 수 없습니다.");
        }
        OrderItem item = new OrderItem(menuId, menuName, unitPrice, quantity);
        item.assignOrder(this);
        this.items.add(item);
        this.totalPrice += item.getSubtotal();
    }

    /**
     * 주문 확정.
     * 이 시점에 재고 차감이 일어남 (Service에서 Menu에 위임).
     */
    public void confirm() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("PENDING 상태가 아닌 주문은 확정할 수 없습니다.");
        }
        if (this.items.isEmpty()) {
            throw new IllegalStateException("빈 주문은 확정할 수 없습니다.");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    /** 서빙 완료 처리 */
    public void complete() {
        if (this.status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("CONFIRMED 상태가 아닌 주문은 완료할 수 없습니다.");
        }
        this.status = OrderStatus.COMPLETED;
    }

    /** 주문 취소 — COMPLETED 상태에서는 취소 불가 */
    public void cancel() {
        if (this.status == OrderStatus.COMPLETED) {
            throw new IllegalStateException("이미 서빙 완료된 주문은 취소할 수 없습니다.");
        }
        if (this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }
        this.status = OrderStatus.CANCELLED;
    }

    /** 재고 차감이 필요한 상태인지 (CONFIRMED 전환 시) */
    public boolean needsStockDeduction() {
        return this.status == OrderStatus.CONFIRMED;
    }

    /** 재고 복구가 필요한 상태인지 (CONFIRMED → CANCELLED) */
    public boolean needsStockRestore(OrderStatus previousStatus) {
        return this.status == OrderStatus.CANCELLED
                && previousStatus == OrderStatus.CONFIRMED;
    }
}