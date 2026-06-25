package com.joonoh.sushiorder.domain.restauranttable.controller;

import com.joonoh.sushiorder.domain.restauranttable.dto.RestaurantTableResponse;
import com.joonoh.sushiorder.domain.restauranttable.service.RestaurantTableService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 직원 앱 매장 평면도 화면용 — STAFF/ADMIN 누구나 조회 가능 (AdminRestaurantTableController는 ADMIN 전용 CRUD) */
@RestController
@RequestMapping("/api/v1/table")
@RequiredArgsConstructor
public class TableController {

    private final RestaurantTableService restaurantTableService;

    @GetMapping
    public ApiResponse<List<RestaurantTableResponse>> getAllTables() {
        return ApiResponse.success(restaurantTableService.getAllTables());
    }
}
