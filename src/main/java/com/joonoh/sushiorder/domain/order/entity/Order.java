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
@Table(name = "orders", indexes = { // 'order'는 SQL 예약어라 복수형 사용
        @Index(name = "idx_orders_table_id", columnList = "table_id"),
        @Index(name = "idx_orders_session_id", columnList = "session_id"),
        @Index(name = "idx_orders_status", columnList = "status"),
        @Index(name = "idx_orders_created_at", columnList = "created_at")
})
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
    public void addItem(Long menuId, String menuName, int unitPrice, int quantity, Long stationId) {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("확정/취소된 주문은 수정할 수 없습니다.");
        }
        OrderItem item = new OrderItem(menuId, menuName, unitPrice, quantity, stationId);
        item.assignOrder(this);
        this.items.add(item);
        this.totalPrice += item.getSubtotal();
    }

    /**
     * 주문 전체 확정 (station 구분 없이 PENDING인 메뉴 전부).
     * 이 시점에 재고 차감이 일어남 (Service에서 Menu에 위임) — 반환된 item들만 차감 대상.
     */
    public List<OrderItem> confirm() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("PENDING 상태가 아닌 주문은 확정할 수 없습니다.");
        }
        if (this.items.isEmpty()) {
            throw new IllegalStateException("빈 주문은 확정할 수 없습니다.");
        }
        List<OrderItem> confirmed = itemsWithStatus(OrderStatus.PENDING);
        confirmed.forEach(OrderItem::confirm);
        recalcStatus();
        return confirmed;
    }

    /** 주문 전체 완료 처리 (station 구분 없이 CONFIRMED인 메뉴 전부) */
    public List<OrderItem> complete() {
        if (this.status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("CONFIRMED 상태가 아닌 주문은 완료할 수 없습니다.");
        }
        List<OrderItem> completed = itemsWithStatus(OrderStatus.CONFIRMED);
        completed.forEach(OrderItem::complete);
        recalcStatus();
        return completed;
    }

    /**
     * 주문 전체 취소 — COMPLETED 상태에서는 취소 불가.
     * 이미 서빙 완료된 개별 메뉴가 있어도 그 메뉴는 그대로 두고 나머지(PENDING/CONFIRMED)만 취소한다.
     * 반환값은 재고 복구가 필요한(취소 전 CONFIRMED였던) item 목록.
     */
    public List<OrderItem> cancel() {
        if (this.status == OrderStatus.COMPLETED) {
            throw new IllegalStateException("이미 서빙 완료된 주문은 취소할 수 없습니다.");
        }
        if (this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }
        List<OrderItem> toCancel = this.items.stream()
                .filter(item -> item.getStatus() == OrderStatus.PENDING || item.getStatus() == OrderStatus.CONFIRMED)
                .toList();
        List<OrderItem> needStockRestore = toCancel.stream()
                .filter(OrderItem::wasStockDeducted)
                .toList();
        toCancel.forEach(OrderItem::cancel);
        recalcStatus();
        recalcTotalPrice();
        return needStockRestore;
    }

    // ===== station별 부분 접수/완료/취소 =====

    /** 해당 station이 담당하는 메뉴 중 PENDING인 것만 확정 — 반환된 item만 재고 차감 대상 */
    public List<OrderItem> confirmItemsByStation(Long stationId) {
        List<OrderItem> targets = itemsWithStatusAndStation(OrderStatus.PENDING, stationId);
        if (targets.isEmpty()) {
            throw new IllegalStateException("해당 station에 접수 대기 중인 메뉴가 없습니다.");
        }
        targets.forEach(OrderItem::confirm);
        recalcStatus();
        return targets;
    }

    /** 해당 station이 담당하는 메뉴 중 CONFIRMED인 것만 완료 처리 */
    public List<OrderItem> completeItemsByStation(Long stationId) {
        List<OrderItem> targets = itemsWithStatusAndStation(OrderStatus.CONFIRMED, stationId);
        if (targets.isEmpty()) {
            throw new IllegalStateException("해당 station에 조리 중인 메뉴가 없습니다.");
        }
        targets.forEach(OrderItem::complete);
        recalcStatus();
        return targets;
    }

    /**
     * 해당 station이 담당하는 메뉴 중 아직 완료/취소되지 않은 것만 부분 취소
     * (ex. 재료 소진으로 그 station 메뉴만 취소, 다른 station 메뉴는 그대로 진행).
     * 반환값은 재고 복구가 필요한(취소 전 CONFIRMED였던) item 목록.
     */
    public List<OrderItem> cancelItemsByStation(Long stationId) {
        List<OrderItem> targets = this.items.stream()
                .filter(item -> item.getStationId() != null && item.getStationId().equals(stationId))
                .filter(item -> item.getStatus() == OrderStatus.PENDING || item.getStatus() == OrderStatus.CONFIRMED)
                .toList();
        if (targets.isEmpty()) {
            throw new IllegalStateException("해당 station에 취소할 수 있는 메뉴가 없습니다.");
        }
        List<OrderItem> needStockRestore = targets.stream()
                .filter(OrderItem::wasStockDeducted)
                .toList();
        targets.forEach(OrderItem::cancel);
        recalcStatus();
        recalcTotalPrice();
        return needStockRestore;
    }

    private List<OrderItem> itemsWithStatus(OrderStatus status) {
        return this.items.stream().filter(item -> item.getStatus() == status).toList();
    }

    private List<OrderItem> itemsWithStatusAndStation(OrderStatus status, Long stationId) {
        return this.items.stream()
                .filter(item -> item.getStatus() == status)
                .filter(item -> item.getStationId() != null && item.getStationId().equals(stationId))
                .toList();
    }

    /**
     * item들의 상태를 보고 주문 전체 상태를 다시 계산한다 (CANCELLED인 item은 계산에서 제외).
     * - 취소 안 된 item이 하나도 없으면(전부 취소) → CANCELLED
     * - 취소 안 된 item 중 하나라도 PENDING이면 → PENDING
     * - 취소 안 된 item이 전부 COMPLETED면 → COMPLETED
     * - 그 외(PENDING 없음, 일부만 COMPLETED) → CONFIRMED
     */
    private void recalcStatus() {
        List<OrderItem> active = this.items.stream()
                .filter(item -> item.getStatus() != OrderStatus.CANCELLED)
                .toList();
        if (active.isEmpty()) {
            this.status = OrderStatus.CANCELLED;
        } else if (active.stream().anyMatch(item -> item.getStatus() == OrderStatus.PENDING)) {
            this.status = OrderStatus.PENDING;
        } else if (active.stream().allMatch(item -> item.getStatus() == OrderStatus.COMPLETED)) {
            this.status = OrderStatus.COMPLETED;
        } else {
            this.status = OrderStatus.CONFIRMED;
        }
    }

    /** 취소된 item을 제외한 금액만 합산 — 부분 취소 시 손님은 실제로 받은 만큼만 결제 */
    private void recalcTotalPrice() {
        this.totalPrice = this.items.stream()
                .filter(item -> item.getStatus() != OrderStatus.CANCELLED)
                .mapToInt(OrderItem::getSubtotal)
                .sum();
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