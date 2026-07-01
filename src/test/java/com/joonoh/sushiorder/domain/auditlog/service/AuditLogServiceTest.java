package com.joonoh.sushiorder.domain.auditlog.service;

import com.joonoh.sushiorder.domain.auditlog.dto.AuditLogResponse;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditAction;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditResult;
import com.joonoh.sushiorder.domain.auditlog.repository.AuditLogRepository;
import com.joonoh.sushiorder.global.audit.AuditEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuditLogServiceTest {

    @Autowired private AuditLogService auditLogService;
    @Autowired private AuditLogRepository auditLogRepository;

    private String actorName;

    @AfterEach
    void tearDown() {
        if (actorName != null) {
            auditLogService.search(null, null, actorName, null, PageRequest.of(0, 20))
                    .forEach(log -> auditLogRepository.deleteById(log.getId()));
            actorName = null;
        }
    }

    @Test
    @DisplayName("record()로 저장한 감사 로그는 검색 결과에 그대로 나온다")
    void record_thenSearch_returnsSavedEntry() {
        actorName = uniqueActorName();
        auditLogService.record(entry(actorName, AuditAction.TABLE_OCCUPIED, AuditResult.SUCCESS, 42L));

        Page<AuditLogResponse> page = auditLogService.search(null, null, actorName, null, PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(1);
        AuditLogResponse saved = page.getContent().get(0);
        assertThat(saved.getAction()).isEqualTo(AuditAction.TABLE_OCCUPIED);
        assertThat(saved.getResult()).isEqualTo(AuditResult.SUCCESS);
        assertThat(saved.getTableId()).isEqualTo(42L);
    }

    @Test
    @DisplayName("action으로 필터링하면 일치하는 로그만 나온다")
    void search_filterByAction_excludesOtherActions() {
        actorName = uniqueActorName();
        auditLogService.record(entry(actorName, AuditAction.TABLE_OCCUPIED, AuditResult.SUCCESS, 1L));
        auditLogService.record(entry(actorName, AuditAction.TABLE_RELEASED, AuditResult.SUCCESS, 1L));

        Page<AuditLogResponse> page = auditLogService.search(
                AuditAction.TABLE_RELEASED, null, actorName, null, PageRequest.of(0, 10));

        assertThat(page.getContent())
                .extracting(AuditLogResponse::getAction)
                .containsOnly(AuditAction.TABLE_RELEASED);
    }

    @Test
    @DisplayName("result로 필터링하면 실패 로그만 나온다")
    void search_filterByResult_returnsOnlyFailures() {
        actorName = uniqueActorName();
        auditLogService.record(entry(actorName, AuditAction.STAFF_LOGIN, AuditResult.SUCCESS, null));
        auditLogService.record(entry(actorName, AuditAction.STAFF_LOGIN, AuditResult.FAILURE, null));

        Page<AuditLogResponse> page = auditLogService.search(
                null, AuditResult.FAILURE, actorName, null, PageRequest.of(0, 10));

        assertThat(page.getContent())
                .extracting(AuditLogResponse::getResult)
                .containsOnly(AuditResult.FAILURE);
    }

    private String uniqueActorName() {
        return "test-actor-" + UUID.randomUUID();
    }

    private AuditEntry entry(String actorName, AuditAction action, AuditResult result, Long tableId) {
        return AuditEntry.builder()
                .actorName(actorName)
                .action(action)
                .result(result)
                .entityType("TEST")
                .tableId(tableId)
                .build();
    }
}
