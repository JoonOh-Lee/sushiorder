package com.joonoh.sushiorder.domain.auditlog.repository;

import com.joonoh.sushiorder.domain.auditlog.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, AuditLogRepositoryCustom {
}
