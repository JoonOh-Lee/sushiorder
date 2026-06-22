package com.joonoh.sushiorder.domain.order.controller;

import com.joonoh.sushiorder.domain.order.dto.OrderResponse;
import com.joonoh.sushiorder.domain.order.dto.PlaceOrderRequest;
import com.joonoh.sushiorder.domain.order.service.OrderService;
import com.joonoh.sushiorder.domain.session.interceptor.SessionTokenInterceptor;
import com.joonoh.sushiorder.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 손님 주문 접수 (PENDING 상태로 저장).
     * tableId/sessionId는 body가 아니라 SessionTokenInterceptor가 검증한 토큰에서 가져온다 —
     * 클라이언트가 다른 테이블/세션 ID를 적어 보내도 무시된다.
     */
    @PostMapping
    public ApiResponse<OrderResponse> placeOrder(
            @RequestAttribute(SessionTokenInterceptor.TABLE_ID_ATTRIBUTE) Long tableId,
            @RequestAttribute(SessionTokenInterceptor.SESSION_ID_ATTRIBUTE) Long sessionId,
            @Valid @RequestBody PlaceOrderRequest request) {
        return ApiResponse.success(orderService.placeOrder(tableId, sessionId, request));
    }

    /** 본인 주문 단건 조회 — 토큰의 세션이 아니면 조회 불가 */
    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getOrder(
            @RequestAttribute(SessionTokenInterceptor.SESSION_ID_ATTRIBUTE) Long sessionId,
            @PathVariable Long orderId) {
        return ApiResponse.success(orderService.getOrder(orderId, sessionId));
    }

    /** 본인 세션의 모든 주문 조회 (주문 내역) */
    @GetMapping("/session")
    public ApiResponse<List<OrderResponse>> getOrdersBySession(
            @RequestAttribute(SessionTokenInterceptor.SESSION_ID_ATTRIBUTE) Long sessionId) {
        return ApiResponse.success(orderService.getOrdersBySessionId(sessionId));
    }
}