package com.joonoh.sushiorder.domain.restauranttable.exception;

public class RestaurantTableNotFoundException extends RuntimeException {

    public RestaurantTableNotFoundException(Long id) {
        super("테이블을 찾을 수 없습니다. id=" + id);
    }
}
