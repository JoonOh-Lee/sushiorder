package com.joonoh.sushiorder.domain.restauranttable.dto;

import com.joonoh.sushiorder.domain.restauranttable.entity.RestaurantTable;
import com.joonoh.sushiorder.domain.restauranttable.entity.TableStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestaurantTableResponse {
    private Long id;
    private int tableNumber;
    private int seatCount;
    private TableStatus status;

    public static RestaurantTableResponse from(RestaurantTable table) {
        return RestaurantTableResponse.builder()
                .id(table.getId())
                .tableNumber(table.getTableNumber())
                .seatCount(table.getSeatCount())
                .status(table.getStatus())
                .build();
    }
}
