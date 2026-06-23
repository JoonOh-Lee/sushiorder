package com.joonoh.sushiorder.domain.menu.entity;

import com.joonoh.sushiorder.domain.station.entity.Station;
import com.joonoh.sushiorder.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menu")
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(length = 255)
    private String description;
    @Column(nullable = false)
    private int price;
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "ingredients", length = 500)
    private String ingredients;
    @Column(name = "allergy_info", length = 255)
    private String allergyInfo;

    @Column(name="stock_count")
    private Integer stockCount;
    @Column(name="is_active", nullable = false)
    private boolean active;

    @Column(name = "like_count", nullable = false)
    private int likeCount;
    @Column(name = "dislike_count", nullable = false)
    private int dislikeCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MenuCategory category;
    @Column(name = "station_id")
    private Long stationId;

//    @Version
    private Long version;

    @Builder
    private Menu(String name, String description, int price,
                 MenuCategory category, String imageUrl, String ingredients, String allergyInfo,
                 Integer stockCount, Long stationId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.allergyInfo = allergyInfo;
        this.stockCount = stockCount;
        this.likeCount = 0;
        this.dislikeCount = 0;
        this.active = true;
        this.stationId = stationId;
    }

    // ===== 비즈니스 로직 =====

    /** 한정 메뉴 여부 */
    public boolean isLimitedStock() {
        return this.stockCount != null;
    }

    /** 주문 가능 여부 — 판매중 상태 + 재고 확인 */
    public boolean isOrderable(int requestedQty) {
        if (!this.active) {
            return false;
        }
        if (!isLimitedStock()) {
            return true; // 무제한 메뉴
        }
        return this.stockCount >= requestedQty;
    }

    /** 재고 차감 — 주문 확정 시 호출 */
    public void decreaseStock(int quantity) {
        if (!isLimitedStock()) {
            return; // 무제한 메뉴는 재고 관리 안 함
        }
        if (this.stockCount < quantity) {
            throw new IllegalStateException(
                    String.format("재고가 부족합니다. 메뉴: %s, 재고: %d, 요청: %d",
                            this.name, this.stockCount, quantity));
        }
        this.stockCount -= quantity;
    }

    /** 재고 복구 — 주문 취소 시 호출 */
    public void restoreStock(int quantity) {
        if (!isLimitedStock()) {
            return;
        }
        this.stockCount += quantity;
    }

    public void increaseLike() {
        this.likeCount++;
    }

    public void increaseDislike() {
        this.dislikeCount++;
    }

    public void changePrice(int newPrice) {
        if (newPrice < 0) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }
        this.price = newPrice;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    // 메뉴 정보 수정
    public void updateInfo(String name, String description, MenuCategory category, String imageUrl,
                            String ingredients, String allergyInfo) {
        this.name = name;
        this.description = description;
        if (category != null) {
            this.category = category;
        }
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.allergyInfo = allergyInfo;
    }
}
