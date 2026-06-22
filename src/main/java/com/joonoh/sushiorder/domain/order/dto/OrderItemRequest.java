package com.joonoh.sushiorder.domain.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemRequest {
    @NotNull
    private Long menuId;

    @NotNull
    @Positive
    private Integer quantity;
}