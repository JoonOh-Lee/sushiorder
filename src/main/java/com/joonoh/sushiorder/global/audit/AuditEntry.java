package com.joonoh.sushiorder.global.audit;

import com.joonoh.sushiorder.domain.auditlog.entity.AuditAction;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditResult;
import lombok.Builder;
import lombok.Getter;

/** AuditAspect → AuditLogService.record() 로 데이터를 넘기는 내부 전달 객체 */
@Getter
@Builder
public class AuditEntry {
    private String actorName;
    private AuditAction action;
    private AuditResult result;
    private String entityType;
    private Long entityId;
    private Long tableId;
    private Integer tableNumber;
    private Long stationId;
    private String stationName;
    private String ipAddress;
    private String userAgent;
    private String metadata;
    private String description;
}
