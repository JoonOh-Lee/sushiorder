package com.joonoh.sushiorder.domain.railsegment.controller;

import com.joonoh.sushiorder.domain.railsegment.dto.RailSegmentResponse;
import com.joonoh.sushiorder.domain.railsegment.service.RailSegmentService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 직원 앱 매장 평면도 화면용 레일 구간 조회 — STAFF/ADMIN 누구나 (AdminRailSegmentController는 ADMIN 전용 on/off) */
@RestController
@RequestMapping("/api/v1/rail-segment")
@RequiredArgsConstructor
public class RailSegmentController {

    private final RailSegmentService railSegmentService;

    @GetMapping
    public ApiResponse<List<RailSegmentResponse>> getAllSegments() {
        return ApiResponse.success(railSegmentService.getAllSegments());
    }
}
