package com.joonoh.sushiorder.domain.menu.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RestockRequest {
    @NotNull
    @Positive
    private Integer quantity;
}
