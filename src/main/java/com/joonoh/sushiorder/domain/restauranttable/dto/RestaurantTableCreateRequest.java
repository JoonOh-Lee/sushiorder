package com.joonoh.sushiorder.domain.restauranttable.dto;

import com.joonoh.sushiorder.domain.restauranttable.entity.SeatType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RestaurantTableCreateRequest {

    @NotNull
    private SeatType seatType;

    @NotNull
    @Min(1)
    private Integer tableNumber;

    @NotNull
    @Min(1)
    private Integer seatCount;
}
