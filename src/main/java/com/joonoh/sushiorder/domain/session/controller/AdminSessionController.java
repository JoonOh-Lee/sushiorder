package com.joonoh.sushiorder.domain.session.controller;

import com.joonoh.sushiorder.domain.session.dto.TableSessionResponse;
import com.joonoh.sushiorder.domain.session.service.TableSessionService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/session")
@RequiredArgsConstructor
public class AdminSessionController {

    private final TableSessionService tableSessionService;

    /** 세션 단건 조회 (테이블 현황 화면 등) */
    @GetMapping("/{sessionId}")
    public ApiResponse<TableSessionResponse> getSession(@PathVariable Long sessionId) {
        return ApiResponse.success(tableSessionService.getSession(sessionId));
    }

    /** 직원 → 테이블 정리/퇴실 처리 */
    @PatchMapping("/{sessionId}/close")
    public ApiResponse<TableSessionResponse> closeSession(@PathVariable Long sessionId) {
        return ApiResponse.success(tableSessionService.closeSession(sessionId));
    }
}
