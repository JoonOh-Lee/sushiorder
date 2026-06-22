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

    /**
     * 패키지 private 생성자 — 외부에서 직접 만들 수 없음.
     * Order.addItem() 을 통해서만 생성 가능 (Aggregate 무결성 보장).
     */
    OrderItem(Long menuId, String menuName, int unitPrice, int quantity) {
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
    }

    /** Order에서 호출 — 양방향 연관관계 설정 */
    void assignOrder(Order order) {
        this.order = order;
    }

    /** 소계 계산 */
    public int getSubtotal() {
        return this.unitPrice * this.quantity;
    }
}