package com.joonoh.sushiorder.domain.floorplan.controller;

import com.joonoh.sushiorder.domain.floorplan.dto.FloorPlanElementCreateRequest;
import com.joonoh.sushiorder.domain.floorplan.dto.FloorPlanElementPositionRequest;
import com.joonoh.sushiorder.domain.floorplan.dto.FloorPlanElementResponse;
import com.joonoh.sushiorder.domain.floorplan.dto.FloorPlanElementUpdateRequest;
import com.joonoh.sushiorder.domain.floorplan.service.FloorPlanElementService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 매장 평면도 위 고정 시설(주방, 레일 등) 배치 — ADMIN 전용 CRUD */
@RestController
@RequestMapping("/api/v1/admin/floor-plan-element")
@RequiredArgsConstructor
public class AdminFloorPlanElementController {

    private final FloorPlanElementService floorPlanElementService;

    @GetMapping
    public ApiResponse<List<FloorPlanElementResponse>> getAllElements() {
        return ApiResponse.success(floorPlanElementService.getAllElements());
    }

    @GetMapping("/{id}")
    public ApiResponse<FloorPlanElementResponse> getElement(@PathVariable Long id) {
        return ApiResponse.success(floorPlanElementService.getElement(id));
    }

    @PostMapping
    public ApiResponse<FloorPlanElementResponse> createElement(@Valid @RequestBody FloorPlanElementCreateRequest request) {
        return ApiResponse.success(floorPlanElementService.createElement(request));
    }

    /** 위치/크기 수정 (드래그 배치) */
    @PatchMapping("/{id}/position")
    public ApiResponse<FloorPlanElementResponse> updatePosition(
            @PathVariable Long id, @Valid @RequestBody FloorPlanElementPositionRequest request) {
        return ApiResponse.success(floorPlanElementService.updatePosition(id, request));
    }

    /** 타입/이름 수정 */
    @PatchMapping("/{id}")
    public ApiResponse<FloorPlanElementResponse> updateInfo(
            @PathVariable Long id, @Valid @RequestBody FloorPlanElementUpdateRequest request) {
        return ApiResponse.success(floorPlanElementService.updateInfo(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteElement(@PathVariable Long id) {
        floorPlanElementService.deleteElement(id);
        return ApiResponse.success();
    }
}
