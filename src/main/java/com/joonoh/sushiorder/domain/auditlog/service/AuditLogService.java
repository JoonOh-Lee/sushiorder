package com.joonoh.sushiorder.domain.auditlog.service;

import com.joonoh.sushiorder.domain.auditlog.dto.AuditLogResponse;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditAction;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditLog;
import com.joonoh.sushiorder.domain.auditlog.repository.AuditLogRepository;
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
    public void record(String actorName, AuditAction action, String entityType, Long entityId, String description) {
        auditLogRepository.save(AuditLog.builder()
                .actorName(actorName)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .description(description)
                .build());
    }

    public Page<AuditLogResponse> search(AuditAction action, String actorName, Pageable pageable) {
        return auditLogRepository.search(action, actorName, pageable)
                .map(AuditLogResponse::from);
    }
}
