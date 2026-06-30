package com.joonoh.sushiorder.domain.auditlog.controller;

import com.joonoh.sushiorder.domain.auditlog.dto.AuditLogResponse;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditAction;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditResult;
import com.joonoh.sushiorder.domain.auditlog.service.AuditLogService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/audit-log")
@RequiredArgsConstructor
public class AdminAuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public ApiResponse<Page<AuditLogResponse>> search(
            @RequestParam(required = false) AuditAction action,
            @RequestParam(required = false) AuditResult result,
            @RequestParam(required = false) String actorName,
            @RequestParam(required = false) Long tableId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(auditLogService.search(action, result, actorName, tableId, pageable));
    }
}
