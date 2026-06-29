package com.joonoh.sushiorder.domain.railsegment.entity;

import com.joonoh.sushiorder.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회전초밥 레일을 좌석 사이를 잇는 구간 단위로 표현. RestaurantTable과는 ID 참조만 한다 — Aggregate 간
 * 경계는 ID 참조, 좌표 계산(두 좌석 중심점을 잇는 선)은 프론트가 RestaurantTable의 x/y로 직접 한다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "rail_segment")
public class RailSegment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sequence_order", nullable = false)
    private int sequenceOrder;

    @Column(name = "from_table_id", nullable = false)
    private Long fromTableId;

    @Column(name = "to_table_id", nullable = false)
    private Long toTableId;

    @Column(nullable = false)
    private boolean active;

    @Builder
    private RailSegment(int sequenceOrder, Long fromTableId, Long toTableId) {
        if (fromTableId == null || toTableId == null) {
            throw new IllegalArgumentException("fromTableId/toTableId는 필수입니다.");
        }
        this.sequenceOrder = sequenceOrder;
        this.fromTableId = fromTableId;
        this.toTableId = toTableId;
        this.active = true;
    }

    // ===== 비즈니스 로직 =====

    public void updateSequenceOrder(int sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    /** 손님이 적을 때 꺼둔 구간을 다시 켠다 */
    public void activate() {
        if (this.active) {
            throw new IllegalStateException("이미 활성화된 구간입니다.");
        }
        this.active = true;
    }

    /** 순환 범위를 줄이기 위해 구간을 끈다 */
    public void deactivate() {
        if (!this.active) {
            throw new IllegalStateException("이미 비활성화된 구간입니다.");
        }
        this.active = false;
    }
}
