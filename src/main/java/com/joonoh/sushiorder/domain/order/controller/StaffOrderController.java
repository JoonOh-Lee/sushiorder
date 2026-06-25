package com.joonoh.sushiorder.domain.order.controller;

import com.joonoh.sushiorder.domain.order.dto.OrderResponse;
import com.joonoh.sushiorder.domain.order.entity.OrderStatus;
import com.joonoh.sushiorder.domain.order.service.OrderService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff/order")
@RequiredArgsConstructor
public class StaffOrderController {

    private final OrderService orderService;

    /** 테이블별 활성 주문 조회 (직원 앱 테이블 현황 화면) */
    @GetMapping("/table/{tableId}/active")
    public ApiResponse<List<OrderResponse>> getActiveOrdersByTable(@PathVariable Long tableId) {
        return ApiResponse.success(orderService.getActiveOrdersByTableId(tableId));
    }

    /**
     * 상태별 주문 조회 (주방용: CONFIRMED 주문 목록 등).
     * status는 여러 개를 줄 수 있다 (ex. ?status=PENDING&status=CONFIRMED) — station 화면에서
     * 주문 접수 즉시(PENDING)부터 조리 중(CONFIRMED)까지 한 번에 보고 싶을 때 사용.
     * stationId를 같이 주면 해당 station이 담당하는 메뉴가 하나라도 포함된 주문만 반환 (station 화면용).
     */
    @GetMapping
    public ApiResponse<List<OrderResponse>> getOrdersByStatus(
            @RequestParam List<OrderStatus> status,
            @RequestParam(required = false) Long stationId) {
        return ApiResponse.success(orderService.getOrdersByStatus(status, stationId));
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