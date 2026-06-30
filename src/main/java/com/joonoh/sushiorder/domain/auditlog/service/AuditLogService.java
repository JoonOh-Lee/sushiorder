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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    /**
     * 감사 로그 저장. REQUIRES_NEW로 비즈니스 트랜잭션과 분리 — 저장 실패가 원래 작업을 롤백시키지 않는다.
     * AuditAspect에서 try/catch로 한 번 더 보호하고 있어 예외는 절대 바깥으로 나가지 않는다.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
