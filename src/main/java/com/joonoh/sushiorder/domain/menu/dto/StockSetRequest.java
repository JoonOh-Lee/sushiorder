package com.joonoh.sushiorder.domain.menu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockSetRequest {
    private Integer stockCount; // null이면 무제한으로 전환
}
