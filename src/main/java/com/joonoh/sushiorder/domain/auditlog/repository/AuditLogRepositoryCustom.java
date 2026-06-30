package com.joonoh.sushiorder.domain.auditlog.repository;

import com.joonoh.sushiorder.domain.auditlog.entity.AuditAction;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditLog;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogRepositoryCustom {
    Page<AuditLog> search(AuditAction action, AuditResult result, String actorName, Long tableId, Pageable pageable);
}
