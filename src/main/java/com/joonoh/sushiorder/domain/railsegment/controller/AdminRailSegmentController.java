package com.joonoh.sushiorder.domain.railsegment.controller;

import com.joonoh.sushiorder.domain.railsegment.dto.RailSegmentResponse;
import com.joonoh.sushiorder.domain.railsegment.service.RailSegmentService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 레일 구간 on/off — 손님이 적을 때 일부 구간을 꺼서 순환 범위를 줄이는 용도. ADMIN 전용 */
@RestController
@RequestMapping("/api/v1/admin/rail-segment")
@RequiredArgsConstructor
public class AdminRailSegmentController {

    private final RailSegmentService railSegmentService;

    @PatchMapping("/{id}/activate")
    public ApiResponse<RailSegmentResponse> activate(@PathVariable Long id) {
        return ApiResponse.success(railSegmentService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ApiResponse<RailSegmentResponse> deactivate(@PathVariable Long id) {
        return ApiResponse.success(railSegmentService.deactivate(id));
    }
}
