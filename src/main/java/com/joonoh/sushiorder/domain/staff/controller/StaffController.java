package com.joonoh.sushiorder.domain.staff.controller;

import com.joonoh.sushiorder.domain.staff.dto.AssignStationRequest;
import com.joonoh.sushiorder.domain.staff.dto.StaffMeResponse;
import com.joonoh.sushiorder.domain.staff.service.StaffService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    /** 로그인한 본인 정보 (현재 배정된 자리 포함) */
    @GetMapping("/me")
    public ApiResponse<StaffMeResponse> getMe(Authentication authentication) {
        return ApiResponse.success(staffService.getMe(authentication.getName()));
    }

    /** 출근 후 본인이 근무할 자리를 직접 지정 — 자리는 날마다 바뀔 수 있음 */
    @PatchMapping("/me/station")
    public ApiResponse<StaffMeResponse> assignStation(
            Authentication authentication,
            @Valid @RequestBody AssignStationRequest request) {
        return ApiResponse.success(staffService.assignStation(authentication.getName(), request.getStationId()));
    }

    /** 근무 시작/종료 토글 — { "on": true/false } */
    @PatchMapping("/me/duty")
    public ApiResponse<StaffMeResponse> setDuty(
            Authentication authentication,
            @RequestBody Map<String, Boolean> body) {
        boolean on = Boolean.TRUE.equals(body.get("on"));
        return ApiResponse.success(staffService.setDuty(authentication.getName(), on));
    }
}
