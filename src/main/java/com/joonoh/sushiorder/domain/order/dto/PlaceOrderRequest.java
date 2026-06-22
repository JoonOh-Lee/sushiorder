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
    // tableId/sessionId는 클라이언트 입력을 신뢰하지 않고
    // SessionTokenInterceptor가 검증한 토큰에서 derive — OrderController 참고
    @NotNull
    private String idempotencyKey;  // 클라이언트가 생성한 UUID

    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;
}