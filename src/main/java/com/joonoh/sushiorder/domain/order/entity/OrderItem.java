package com.joonoh.sushiorder.domain.order.entity;

import com.joonoh.sushiorder.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_item")
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    // 주문 당시 메뉴 정보 스냅샷 (메뉴가 나중에 바뀌어도 보존)
    @Column(name = "menu_name", nullable = false, length = 50)
    private String menuName;

    @Column(name = "unit_price", nullable = false)
    private int unitPrice;

    @Column(nullable = false)
    private int quantity;

    // 주문 당시 메뉴가 속한 station 스냅샷 — station별 개별 접수/완료/취소 처리에 사용
    @Column(name = "station_id")
    private Long stationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    /**
     * 패키지 private 생성자 — 외부에서 직접 만들 수 없음.
     * Order.addItem() 을 통해서만 생성 가능 (Aggregate 무결성 보장).
     */
    OrderItem(Long menuId, String menuName, int unitPrice, int quantity, Long stationId) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }
        this.menuId = menuId;
        this.menuName = menuName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.stationId = stationId;
        this.status = OrderStatus.PENDING;
    }

    /** Order에서 호출 — 양방향 연관관계 설정 */
    void assignOrder(Order order) {
        this.order = order;
    }

    /** 소계 계산 */
    public int getSubtotal() {
        return this.unitPrice * this.quantity;
    }

    // ===== item 단위 상태 전이 — station별 개별 접수/완료/취소 처리용 =====

    void confirm() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    String.format("접수 대기 중인 메뉴가 아닙니다. 메뉴: %s", this.menuName));
        }
        this.status = OrderStatus.CONFIRMED;
    }

    void complete() {
        if (this.status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException(
                    String.format("조리 중인 메뉴가 아닙니다. 메뉴: %s", this.menuName));
        }
        this.status = OrderStatus.COMPLETED;
    }

    /** 이미 완료/취소된 메뉴는 취소 불가 — 그 외(PENDING/CONFIRMED)는 취소 가능 */
    void cancel() {
        if (this.status == OrderStatus.COMPLETED) {
            throw new IllegalStateException(
                    String.format("이미 서빙 완료된 메뉴는 취소할 수 없습니다. 메뉴: %s", this.menuName));
        }
        if (this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException(
                    String.format("이미 취소된 메뉴입니다. 메뉴: %s", this.menuName));
        }
        this.status = OrderStatus.CANCELLED;
    }

    /** 취소 직전 재고가 차감된 상태였는지 (CONFIRMED였는지) — 취소 전에 호출해 service에서 재고 복구 여부 판단 */
    boolean wasStockDeducted() {
        return this.status == OrderStatus.CONFIRMED;
    }
}