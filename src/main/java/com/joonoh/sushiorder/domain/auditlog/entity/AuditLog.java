package com.joonoh.sushiorder.domain.auditlog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "audit_log", indexes = {
        // search()가 항상 createdAt desc로 정렬 + action/tableId를 optional 필터로 씀
        @Index(name = "idx_audit_log_created_at", columnList = "created_at"),
        @Index(name = "idx_audit_log_table_id", columnList = "table_id"),
        @Index(name = "idx_audit_log_action", columnList = "action")
})
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // null이면 손님 행동 (QR 세션, JWT 없음)
    @Column(name = "actor_name", length = 50)
    private String actorName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AuditAction action;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AuditResult result;

    @Column(name = "entity_type", nullable = false, length = 30)
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;

    // ─── 1순위: 필터링/집계에 필요한 구조 필드 ─────────────────────────────
    @Column(name = "table_id")
    private Long tableId;

    @Column(name = "table_number")
    private Integer tableNumber;

    @Column(name = "station_id")
    private Long stationId;

    // 스테이션 이름 스냅샷 — 이름이 바뀌어도 당시 기록이 보존됨
    @Column(name = "station_name", length = 50)
    private String stationName;

    // ─── 2순위: 보안/추적 필드 ─────────────────────────────────────────────
    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    // 3순위: 기기 구분
    @Column(name = "user_agent", length = 300)
    private String userAgent;

    // 액션별 자유 컨텍스트 — JSON 문자열 (itemCount, totalPrice 등)
    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Column(length = 255)
    private String description;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private AuditLog(String actorName, AuditAction action, AuditResult result,
                     String entityType, Long entityId,
                     Long tableId, Integer tableNumber,
                     Long stationId, String stationName,
                     String ipAddress, String userAgent,
                     String metadata, String description) {
        this.actorName = actorName;
        this.action = action;
        this.result = result;
        this.entityType = entityType;
        this.entityId = entityId;
        this.tableId = tableId;
        this.tableNumber = tableNumber;
        this.stationId = stationId;
        this.stationName = stationName;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.metadata = metadata;
        this.description = description;
    }
}
