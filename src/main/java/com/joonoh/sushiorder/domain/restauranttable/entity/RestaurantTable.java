package com.joonoh.sushiorder.domain.restauranttable.entity;

import com.joonoh.sushiorder.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "restaurant_table")
public class RestaurantTable extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_number", nullable = false, unique = true)
    private int tableNumber;

    @Column(name = "seat_count", nullable = false)
    private int seatCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TableStatus status;

    @Builder
    private RestaurantTable(int tableNumber, int seatCount) {
        if (tableNumber <= 0) {
            throw new IllegalArgumentException("테이블 번호는 1 이상이어야 합니다.");
        }
        if (seatCount <= 0) {
            throw new IllegalArgumentException("좌석 수는 1 이상이어야 합니다.");
        }
        this.tableNumber = tableNumber;
        this.seatCount = seatCount;
        this.status = TableStatus.EMPTY;
    }

    // ===== 비즈니스 로직 =====

    /** 손님 착석 — 빈 자리에서만 가능 */
    public void occupy() {
        if (this.status != TableStatus.EMPTY) {
            throw new IllegalStateException(
                    String.format("빈 자리가 아닙니다. 현재 상태: %s", this.status.getDisplayName()));
        }
        this.status = TableStatus.OCCUPIED;
    }

    /** 계산 완료 — 자리 비움 */
    public void release() {
        if (this.status == TableStatus.EMPTY) {
            throw new IllegalStateException("이미 빈 자리입니다.");
        }
        this.status = TableStatus.EMPTY;
    }

    public void reserve() {
        if (this.status != TableStatus.EMPTY) {
            throw new IllegalStateException("빈 자리만 예약 가능합니다.");
        }
        this.status = TableStatus.RESERVED;
    }

    public void cancelReservation() {
        if (this.status != TableStatus.RESERVED) {
            throw new IllegalStateException("예약 상태가 아닙니다.");
        }
        this.status = TableStatus.EMPTY;
    }
}