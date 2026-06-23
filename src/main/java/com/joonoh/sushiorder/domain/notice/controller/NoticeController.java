package com.joonoh.sushiorder.domain.notice.controller;

import com.joonoh.sushiorder.domain.notice.dto.NoticeResponse;
import com.joonoh.sushiorder.domain.notice.service.NoticeService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ApiResponse<List<NoticeResponse>> getActiveNotices() {
        return ApiResponse.success(noticeService.getActiveNotices());
    }
}
