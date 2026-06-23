package com.joonoh.sushiorder.domain.notice.controller;

import com.joonoh.sushiorder.domain.notice.dto.NoticeCreateRequest;
import com.joonoh.sushiorder.domain.notice.dto.NoticeResponse;
import com.joonoh.sushiorder.domain.notice.dto.NoticeUpdateRequest;
import com.joonoh.sushiorder.domain.notice.service.NoticeService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/notice")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ApiResponse<List<NoticeResponse>> getAllNotices() {
        return ApiResponse.success(noticeService.getAllNotices());
    }

    @PostMapping
    public ApiResponse<NoticeResponse> createNotice(@Valid @RequestBody NoticeCreateRequest request) {
        return ApiResponse.success(noticeService.createNotice(request));
    }

    @PatchMapping("/{id}")
    public ApiResponse<Void> updateNotice(@PathVariable Long id,
                                           @Valid @RequestBody NoticeUpdateRequest request) {
        noticeService.updateNotice(id, request);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/pin")
    public ApiResponse<Void> pinNotice(@PathVariable Long id) {
        noticeService.pinNotice(id);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/unpin")
    public ApiResponse<Void> unpinNotice(@PathVariable Long id) {
        noticeService.unpinNotice(id);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/activate")
    public ApiResponse<Void> activateNotice(@PathVariable Long id) {
        noticeService.activateNotice(id);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/deactivate")
    public ApiResponse<Void> deactivateNotice(@PathVariable Long id) {
        noticeService.deactivateNotice(id);
        return ApiResponse.success();
    }
}
