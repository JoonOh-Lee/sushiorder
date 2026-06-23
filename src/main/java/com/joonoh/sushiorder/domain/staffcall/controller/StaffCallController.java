package com.joonoh.sushiorder.domain.staffcall.controller;

import com.joonoh.sushiorder.domain.staffcall.dto.StaffCallResponse;
import com.joonoh.sushiorder.domain.staffcall.service.StaffCallService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff/call")
@RequiredArgsConstructor
public class StaffCallController {

    private final StaffCallService staffCallService;

    /** 처리 대기 중인 호출 목록 — 직원이면 누구나 조회 가능 */
    @GetMapping
    public ApiResponse<List<StaffCallResponse>> getRequestedCalls() {
        return ApiResponse.success(staffCallService.getRequestedCalls());
    }

    @PatchMapping("/{id}/resolve")
    public ApiResponse<Void> resolveCall(@PathVariable Long id) {
        staffCallService.resolveCall(id);
        return ApiResponse.success();
    }
}
