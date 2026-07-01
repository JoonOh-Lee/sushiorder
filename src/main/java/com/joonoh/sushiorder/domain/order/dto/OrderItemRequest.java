package com.joonoh.sushiorder.domain.order.dto;

import jakarta.validation.constraints.Max;
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
    @Max(99) // 한 테이블 주문 한 줄 기준 상한 — 이상값/오버플로 방지용 방어선
    private Integer quantity;
}