package com.joonoh.sushiorder.global.audit;

import com.joonoh.sushiorder.domain.auditlog.entity.AuditAction;
import com.joonoh.sushiorder.domain.auditlog.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogService auditLogService;

    @AfterReturning(pointcut = "@annotation(audit)", returning = "result")
    public void recordAudit(JoinPoint jp, Audit audit, Object result) {
        try {
            String actorName = resolveActorName(audit.action(), result);
            Long entityId = extractEntityId(result);
            String description = buildDescription(audit.action(), entityId, actorName);

            auditLogService.record(actorName, audit.action(), audit.entityType(), entityId, description);
        } catch (Exception e) {
            log.warn("감사 로그 기록 실패 — action={}, cause={}", audit.action(), e.getMessage());
        }
    }

    /**
     * 직원 행동은 SecurityContext에서 username을 읽는다.
     * STAFF_LOGIN은 로그인 시점에 SecurityContext가 없으므로 LoginResponse에서 직접 추출.
     * 손님 행동(QR 세션)은 SecurityContext가 anonymous 상태 → null 반환.
     */
    private String resolveActorName(AuditAction action, Object result) {
        if (action == AuditAction.STAFF_LOGIN) {
            return extractStringField(result, "getUsername");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return auth.getName();
    }

    private Long extractEntityId(Object result) {
        if (result == null) return null;
        try {
            return (Long) result.getClass().getMethod("getId").invoke(result);
        } catch (Exception e) {
            return null;
        }
    }

    private String extractStringField(Object obj, String getterName) {
        if (obj == null) return null;
        try {
            return (String) obj.getClass().getMethod(getterName).invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }

    private String buildDescription(AuditAction action, Long entityId, String actorName) {
        return switch (action) {
            case ORDER_PLACED -> "주문 #" + entityId + " 접수 (손님)";
            case ORDER_CONFIRMED -> "주문 #" + entityId + " 확정";
            case ORDER_COMPLETED -> "주문 #" + entityId + " 완료";
            case ORDER_CANCELLED -> "주문 #" + entityId + " 취소";
            case TABLE_OCCUPIED -> "세션 #" + entityId + " 생성 (테이블 점유)";
            case TABLE_RELEASED -> "세션 #" + entityId + " 종료 (테이블 해제)";
            case STAFF_LOGIN -> (actorName != null ? actorName : "알 수 없음") + " 로그인";
        };
    }
}
