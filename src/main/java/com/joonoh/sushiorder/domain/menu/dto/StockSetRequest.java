package com.joonoh.sushiorder.domain.menu.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockSetRequest {
    @Min(0)
    private Integer stockCount; // null이면 무제한으로 전환
}
