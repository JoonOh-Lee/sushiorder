package com.joonoh.sushiorder.domain.restauranttable.controller;

import com.joonoh.sushiorder.domain.restauranttable.dto.RestaurantTableCreateRequest;
import com.joonoh.sushiorder.domain.restauranttable.dto.RestaurantTableResponse;
import com.joonoh.sushiorder.domain.restauranttable.service.RestaurantTableService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/table")
@RequiredArgsConstructor
public class AdminRestaurantTableController {

    private final RestaurantTableService restaurantTableService;

    /** 테이블 현황 전체 조회 (직원 앱 메인 화면) */
    @GetMapping
    public ApiResponse<List<RestaurantTableResponse>> getAllTables() {
        return ApiResponse.success(restaurantTableService.getAllTables());
    }

    @GetMapping("/{id}")
    public ApiResponse<RestaurantTableResponse> getTable(@PathVariable Long id) {
        return ApiResponse.success(restaurantTableService.getTable(id));
    }

    @PostMapping
    public ApiResponse<RestaurantTableResponse> createTable(@Valid @RequestBody RestaurantTableCreateRequest request) {
        return ApiResponse.success(restaurantTableService.createTable(request));
    }

    /** 워크인 손님 수동 착석 (QR 세션 없이) */
    @PatchMapping("/{id}/occupy")
    public ApiResponse<Void> occupyTable(@PathVariable Long id) {
        restaurantTableService.occupyTable(id);
        return ApiResponse.success();
    }

    /** 계산 완료 — 자리 비움 */
    @PatchMapping("/{id}/release")
    public ApiResponse<Void> releaseTable(@PathVariable Long id) {
        restaurantTableService.releaseTable(id);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/reserve")
    public ApiResponse<Void> reserveTable(@PathVariable Long id) {
        restaurantTableService.reserveTable(id);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/cancel-reservation")
    public ApiResponse<Void> cancelReservation(@PathVariable Long id) {
        restaurantTableService.cancelReservation(id);
        return ApiResponse.success();
    }
}
