package com.joonoh.sushiorder.domain.restauranttable.repository;

import com.joonoh.sushiorder.domain.restauranttable.entity.RestaurantTable;
import com.joonoh.sushiorder.domain.restauranttable.entity.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    /** 테이블 생성 시 중복 번호 체크 — 번호는 좌석 타입별로 따로 매겨진다 */
    boolean existsBySeatTypeAndTableNumber(SeatType seatType, int tableNumber);
}
