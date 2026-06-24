package com.joonoh.sushiorder.domain.staffcall.controller;

import com.joonoh.sushiorder.domain.session.interceptor.SessionTokenInterceptor;
import com.joonoh.sushiorder.domain.staffcall.dto.StaffCallRequest;
import com.joonoh.sushiorder.domain.staffcall.dto.StaffCallResponse;
import com.joonoh.sushiorder.domain.staffcall.service.StaffCallService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/call")
@RequiredArgsConstructor
public class CallController {

    private final StaffCallService staffCallService;

    /**
     * 손님이 직원 호출 (물 리필/문의 등).
     * tableId/sessionId는 body가 아니라 SessionTokenInterceptor가 검증한 토큰에서 가져온다.
     */
    @PostMapping
    public ApiResponse<StaffCallResponse> call(
            @RequestAttribute(SessionTokenInterceptor.TABLE_ID_ATTRIBUTE) Long tableId,
            @RequestAttribute(SessionTokenInterceptor.SESSION_ID_ATTRIBUTE) Long sessionId,
            @Valid @RequestBody StaffCallRequest request) {
        return ApiResponse.success(staffCallService.createCall(tableId, sessionId, request.getType(), request.getItemName()));
    }
}
