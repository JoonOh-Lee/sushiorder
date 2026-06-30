package com.joonoh.sushiorder.domain.auditlog.repository;

import com.joonoh.sushiorder.domain.auditlog.entity.AuditAction;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditLog;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditResult;
import com.joonoh.sushiorder.domain.auditlog.entity.QAuditLog;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class AuditLogRepositoryImpl implements AuditLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AuditLog> search(AuditAction action, AuditResult result, String actorName, Long tableId, Pageable pageable) {
        QAuditLog log = QAuditLog.auditLog;

        List<AuditLog> content = queryFactory
                .selectFrom(log)
                .where(eqAction(action), eqResult(result), eqActorName(actorName), eqTableId(tableId))
                .orderBy(log.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(log.count())
                .from(log)
                .where(eqAction(action), eqResult(result), eqActorName(actorName), eqTableId(tableId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    private BooleanExpression eqAction(AuditAction action) {
        return action != null ? QAuditLog.auditLog.action.eq(action) : null;
    }

    private BooleanExpression eqResult(AuditResult result) {
        return result != null ? QAuditLog.auditLog.result.eq(result) : null;
    }

    private BooleanExpression eqActorName(String actorName) {
        return (actorName != null && !actorName.isBlank()) ? QAuditLog.auditLog.actorName.eq(actorName) : null;
    }

    private BooleanExpression eqTableId(Long tableId) {
        return tableId != null ? QAuditLog.auditLog.tableId.eq(tableId) : null;
    }
}
