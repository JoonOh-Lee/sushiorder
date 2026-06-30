package com.joonoh.sushiorder.domain.auditlog.dto;

import com.joonoh.sushiorder.domain.auditlog.entity.AuditAction;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditLog;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditResult;
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
    private LocalDateTime createdAt;

    public static AuditLogResponse from(AuditLog log) {
        return AuditLogResponse.builder()
                .id(log.getId())
                .actorName(log.getActorName())
                .action(log.getAction())
                .actionDisplayName(log.getAction().getDisplayName())
                .result(log.getResult())
                .entityType(log.getEntityType())
                .entityId(log.getEntityId())
                .tableId(log.getTableId())
                .tableNumber(log.getTableNumber())
                .stationId(log.getStationId())
                .stationName(log.getStationName())
                .ipAddress(log.getIpAddress())
                .userAgent(log.getUserAgent())
                .metadata(log.getMetadata())
                .description(log.getDescription())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
