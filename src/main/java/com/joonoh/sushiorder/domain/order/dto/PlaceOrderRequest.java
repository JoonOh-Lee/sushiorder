package com.joonoh.sushiorder.domain.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(max = 50) // 비정상적으로 큰 요청(예: 10,000개 아이템) 방지용 방어선
    @Valid
    private List<OrderItemRequest> items;
}