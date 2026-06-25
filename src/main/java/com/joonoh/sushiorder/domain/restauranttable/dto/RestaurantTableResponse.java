package com.joonoh.sushiorder.domain.restauranttable.dto;

import com.joonoh.sushiorder.domain.restauranttable.entity.RestaurantTable;
import com.joonoh.sushiorder.domain.restauranttable.entity.SeatType;
import com.joonoh.sushiorder.domain.restauranttable.entity.TableStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestaurantTableResponse {
    private Long id;
    private SeatType seatType;
    private int tableNumber;
    private int seatCount;
    private TableStatus status;
    private Double x;
    private Double y;
    private Double width;
    private Double height;

    public static RestaurantTableResponse from(RestaurantTable table) {
        return RestaurantTableResponse.builder()
                .id(table.getId())
                .seatType(table.getSeatType())
                .tableNumber(table.getTableNumber())
                .seatCount(table.getSeatCount())
                .status(table.getStatus())
                .x(table.getX())
                .y(table.getY())
                .width(table.getWidth())
                .height(table.getHeight())
                .build();
    }
}
