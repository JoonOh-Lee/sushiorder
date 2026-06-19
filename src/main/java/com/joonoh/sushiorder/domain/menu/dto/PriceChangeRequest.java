package com.joonoh.sushiorder.domain.menu.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PriceChangeRequest {
    @NotNull
    @PositiveOrZero
    private Integer price;
}
