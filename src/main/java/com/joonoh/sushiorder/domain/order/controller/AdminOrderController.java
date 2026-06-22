package com.joonoh.sushiorder.domain.order.controller;

import com.joonoh.sushiorder.domain.order.dto.OrderResponse;
import com.joonoh.sushiorder.domain.order.entity.OrderStatus;
import com.joonoh.sushiorder.domain.order.service.OrderService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    /** 테이블별 활성 주문 조회 (직원 앱 테이블 현황 화면) */
    @GetMapping("/table/{tableId}/active")
    public ApiResponse<List<OrderResponse>> getActiveOrdersByTable(@PathVariable Long tableId) {
        return ApiResponse.success(orderService.getActiveOrdersByTableId(tableId));
    }

    /** 상태별 주문 조회 (주방용: CONFIRMED 주문 목록 등) */
    @GetMapping
    public ApiResponse<List<OrderResponse>> getOrdersByStatus(@RequestParam OrderStatus status) {
        return ApiResponse.success(orderService.getOrdersByStatus(status));
    }

    /** 주문 확정 — 이때 재고 차감 발생 */
    @PatchMapping("/{orderId}/confirm")
    public ApiResponse<OrderResponse> confirmOrder(@PathVariable Long orderId) {
        return ApiResponse.success(orderService.confirmOrder(orderId));
    }

    /** 서빙 완료 처리 */
    @PatchMapping("/{orderId}/complete")
    public ApiResponse<OrderResponse> completeOrder(@PathVariable Long orderId) {
        return ApiResponse.success(orderService.completeOrder(orderId));
    }

    /** 주문 취소 — CONFIRMED였다면 재고 복구도 함께 */
    @PatchMapping("/{orderId}/cancel")
    public ApiResponse<OrderResponse> cancelOrder(@PathVariable Long orderId) {
        return ApiResponse.success(orderService.cancelOrder(orderId));
    }
}