package com.joonoh.sushiorder.domain.auditlog.service;

import com.joonoh.sushiorder.domain.auditlog.dto.AuditLogResponse;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditAction;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditLog;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditResult;
import com.joonoh.sushiorder.domain.auditlog.repository.AuditLogRepository;
import com.joonoh.sushiorder.global.audit.AuditEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    /**
     * 감사 로그 비동기 저장. 비즈니스 스레드를 블로킹하지 않아 동시성 테스트의 커넥션 풀 경합을 방지한다.
     * @Async 메서드는 항상 별도 스레드에서 실행되므로 REQUIRES_NEW 대신 기본 @Transactional로 충분.
     */
    @Async
    @Transactional
    public void record(AuditEntry entry) {
        auditLogRepository.save(AuditLog.builder()
                .actorName(entry.getActorName())
                .action(entry.getAction())
                .result(entry.getResult())
                .entityType(entry.getEntityType())
                .entityId(entry.getEntityId())
                .tableId(entry.getTableId())
                .tableNumber(entry.getTableNumber())
                .stationId(entry.getStationId())
                .stationName(entry.getStationName())
                .ipAddress(entry.getIpAddress())
                .userAgent(entry.getUserAgent())
                .metadata(entry.getMetadata())
                .description(entry.getDescription())
                .build());
    }

    public Page<AuditLogResponse> search(AuditAction action, AuditResult result, String actorName, Long tableId, Pageable pageable) {
        return auditLogRepository.search(action, result, actorName, tableId, pageable)
                .map(AuditLogResponse::from);
    }
}
