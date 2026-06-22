package com.joonoh.sushiorder.domain.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PlaceOrderRequest {
    @NotNull
    private Long tableId;

    @NotNull
    private Long sessionId;

    @NotNull
    private String idempotencyKey;  // 클라이언트가 생성한 UUID

    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;
}