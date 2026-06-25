package com.joonoh.sushiorder.domain.floorplan.entity;

import com.joonoh.sushiorder.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 매장 평면도 위의 테이블이 아닌 고정 시설(주방, 회전초밥 레일 등).
 * RestaurantTable과 달리 화면 좌표가 곧 존재 이유라 위치는 생성 시점부터 필수다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "floor_plan_element")
public class FloorPlanElement extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FloorPlanElementType type;

    @Column(length = 50)
    private String label;

    @Column(nullable = false)
    private Double x;
    @Column(nullable = false)
    private Double y;
    @Column(nullable = false)
    private Double width;
    @Column(nullable = false)
    private Double height;

    @Builder
    private FloorPlanElement(FloorPlanElementType type, String label, Double x, Double y, Double width, Double height) {
        if (type == null) {
            throw new IllegalArgumentException("타입은 필수입니다.");
        }
        this.type = type;
        this.label = label;
        updatePosition(x, y, width, height);
    }

    // ===== 비즈니스 로직 =====

    /** 평면도 위치/크기 수정 — ADMIN이 매장별로 커스텀 배치 */
    public void updatePosition(Double x, Double y, Double width, Double height) {
        validatePercent(x, "x");
        validatePercent(y, "y");
        validatePercent(width, "width");
        validatePercent(height, "height");
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /** 타입/이름 수정 — type을 안 주면 기존 타입 유지 */
    public void updateInfo(FloorPlanElementType type, String label) {
        if (type != null) {
            this.type = type;
        }
        this.label = label;
    }

    private void validatePercent(Double value, String fieldName) {
        if (value == null || value < 0 || value > 100) {
            throw new IllegalArgumentException(fieldName + "는 0~100 사이의 값이어야 합니다.");
        }
    }
}
