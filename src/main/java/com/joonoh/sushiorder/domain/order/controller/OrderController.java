package com.joonoh.sushiorder.domain.order.controller;

import com.joonoh.sushiorder.domain.order.dto.OrderResponse;
import com.joonoh.sushiorder.domain.order.dto.PlaceOrderRequest;
import com.joonoh.sushiorder.domain.order.service.OrderService;
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

    /** 손님 주문 접수 (PENDING 상태로 저장) */
    @PostMapping
    public ApiResponse<OrderResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        return ApiResponse.success(orderService.placeOrder(request));
    }

    /** 본인 주문 단건 조회 */
    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getOrder(@PathVariable Long orderId) {
        return ApiResponse.success(orderService.getOrder(orderId));
    }

    /** 본인 세션의 모든 주문 조회 (주문 내역) */
    @GetMapping("/session/{sessionId}")
    public ApiResponse<List<OrderResponse>> getOrdersBySession(@PathVariable Long sessionId) {
        return ApiResponse.success(orderService.getOrdersBySessionId(sessionId));
    }
}