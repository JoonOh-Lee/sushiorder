package com.joonoh.sushiorder.domain.restauranttable.repository;

import com.joonoh.sushiorder.domain.restauranttable.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    /** 테이블 생성 시 중복 번호 체크 */
    boolean existsByTableNumber(int tableNumber);
}
