package com.joonoh.sushiorder.domain.auditlog.dto;

import com.joonoh.sushiorder.domain.auditlog.entity.AuditAction;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditLog;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AuditLogResponse {
    private Long id;
    private String actorName;
    private AuditAction action;
    private String actionDisplayName;
    private String entityType;
    private Long entityId;
    private String description;
    private LocalDateTime createdAt;

    public static AuditLogResponse from(AuditLog log) {
        return AuditLogResponse.builder()
                .id(log.getId())
                .actorName(log.getActorName())
                .action(log.getAction())
                .actionDisplayName(log.getAction().getDisplayName())
                .entityType(log.getEntityType())
                .entityId(log.getEntityId())
                .description(log.getDescription())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
