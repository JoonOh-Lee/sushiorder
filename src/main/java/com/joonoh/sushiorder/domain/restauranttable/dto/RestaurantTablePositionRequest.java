package com.joonoh.sushiorder.domain.restauranttable.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RestaurantTablePositionRequest {

    @NotNull
    @DecimalMin("0")
    @DecimalMax("100")
    private Double x;

    @NotNull
    @DecimalMin("0")
    @DecimalMax("100")
    private Double y;

    @NotNull
    @DecimalMin("0")
    @DecimalMax("100")
    private Double width;

    @NotNull
    @DecimalMin("0")
    @DecimalMax("100")
    private Double height;
}
