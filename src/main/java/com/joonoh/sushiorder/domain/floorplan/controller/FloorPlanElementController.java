package com.joonoh.sushiorder.domain.floorplan.controller;

import com.joonoh.sushiorder.domain.floorplan.dto.FloorPlanElementResponse;
import com.joonoh.sushiorder.domain.floorplan.service.FloorPlanElementService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 직원 앱 매장 평면도 화면용 — STAFF/ADMIN 누구나 조회 가능 (AdminFloorPlanElementController는 ADMIN 전용 CRUD) */
@RestController
@RequestMapping("/api/v1/floor-plan-element")
@RequiredArgsConstructor
public class FloorPlanElementController {

    private final FloorPlanElementService floorPlanElementService;

    @GetMapping
    public ApiResponse<List<FloorPlanElementResponse>> getAllElements() {
        return ApiResponse.success(floorPlanElementService.getAllElements());
    }
}
