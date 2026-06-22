package com.joonoh.sushiorder.domain.session.controller;

import com.joonoh.sushiorder.domain.session.dto.CreateSessionRequest;
import com.joonoh.sushiorder.domain.session.dto.TableSessionResponse;
import com.joonoh.sushiorder.domain.session.service.TableSessionService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/session")
@RequiredArgsConstructor
public class SessionController {

    private final TableSessionService tableSessionService;

    /** QR 스캔 → 세션 시작 */
    @PostMapping
    public ApiResponse<TableSessionResponse> createSession(@Valid @RequestBody CreateSessionRequest request) {
        return ApiResponse.success(tableSessionService.createSession(request.getTableId()));
    }

    /** 손님이 본인 세션 상태 조회 (QR 토큰 기준) */
    @GetMapping("/{sessionToken}")
    public ApiResponse<TableSessionResponse> getSessionByToken(@PathVariable String sessionToken) {
        return ApiResponse.success(tableSessionService.getSessionByToken(sessionToken));
    }
}
