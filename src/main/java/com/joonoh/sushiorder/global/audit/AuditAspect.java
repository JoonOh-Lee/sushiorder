package com.joonoh.sushiorder.global.audit;

import com.joonoh.sushiorder.domain.auditlog.entity.AuditAction;
import com.joonoh.sushiorder.domain.auditlog.entity.AuditResult;
import com.joonoh.sushiorder.domain.auditlog.service.AuditLogService;
import com.joonoh.sushiorder.domain.staff.repository.StaffRepository;
import com.joonoh.sushiorder.domain.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Order(1) — retry/transaction 프록시(LOWEST_PRECEDENCE)보다 바깥에서 감싼다.
 * 이렇게 해야 retry 재시도가 모두 끝난 뒤의 최종 결과를 단 1번 기록한다.
 */
@Slf4j
@Aspect
@Component
@Order(1)
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogService auditLogService;
    private final StaffRepository staffRepository;
    private final StationRepository stationRepository;
    private final ObjectMapper objectMapper;

    @Around("@annotation(audit)")
    public Object recordAudit(ProceedingJoinPoint pjp, Audit audit) throws Throwable {
        Object result = null;
        Throwable failure = null;
        try {
            result = pjp.proceed();
            return result;
        } catch (Throwable e) {
            failure = e;
            throw e;
        } finally {
            try {
                record(audit, pjp, result, failure);
            } catch (Exception e) {
                log.warn("감사 로그 기록 실패 — action={}, cause={}", audit.action(), e.getMessage());
            }
        }
    }

    private void record(Audit audit, ProceedingJoinPoint pjp, Object result, Throwable failure) {
        AuditResult auditResult = failure == null ? AuditResult.SUCCESS : AuditResult.FAILURE;
        String actorName = resolveActorName(audit.action(), result);
        Long entityId = extractLong(result, "getId");
        Long tableId = resolveTableId(audit, pjp, result);
        Integer tableNumber = resolveTableNumber(result);
        Long stationId = resolveStationId(audit, pjp, actorName);
        String stationName = stationId != null
                ? stationRepository.findById(stationId).map(s -> s.getName()).orElse(null)
                : null;
        String ipAddress = extractIpAddress();
        String userAgent = extractUserAgent();
        String metadata = buildMetadata(audit.action(), result, failure);
        String description = buildDescription(audit.action(), auditResult, entityId, actorName, tableNumber, stationName);

        auditLogService.record(AuditEntry.builder()
                .actorName(actorName)
                .action(audit.action())
                .result(auditResult)
                .entityType(audit.entityType())
                .entityId(entityId)
                .tableId(tableId)
                .tableNumber(tableNumber)
                .stationId(stationId)
                .stationName(stationName)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .metadata(metadata)
                .description(description)
                .build());
    }

    // ─── Actor ────────────────────────────────────────────────────────────────

    /**
     * STAFF_LOGIN: SecurityContext가 아직 미설정 시점 → 리턴값 LoginResponse.getUsername() 사용.
     * 손님 행동: SecurityContext가 anonymous → null.
     * 일반 직원 행동: SecurityContext JWT username.
     */
    private String resolveActorName(AuditAction action, Object result) {
        if (action == AuditAction.STAFF_LOGIN) {
            return extractString(result, "getUsername");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return auth.getName();
    }

    // ─── tableId / tableNumber ─────────────────────────────────────────────────

    private Long resolveTableId(Audit audit, ProceedingJoinPoint pjp, Object result) {
        if (audit.tableIdArgIndex() >= 0) {
            Object[] args = pjp.getArgs();
            if (audit.tableIdArgIndex() < args.length && args[audit.tableIdArgIndex()] instanceof Long l) {
                return l;
            }
        }
        return extractLong(result, "getTableId");
    }

    private Integer resolveTableNumber(Object result) {
        if (result == null) return null;
        try {
            Object val = result.getClass().getMethod("getTableNumber").invoke(result);
            if (val instanceof Integer i) return i != 0 ? i : null;
        } catch (Exception ignored) {}
        return null;
    }

    // ─── stationId ────────────────────────────────────────────────────────────

    private Long resolveStationId(Audit audit, ProceedingJoinPoint pjp, String actorName) {
        // 어노테이션에 명시된 인자 인덱스 우선
        if (audit.stationIdArgIndex() >= 0) {
            Object[] args = pjp.getArgs();
            if (audit.stationIdArgIndex() < args.length && args[audit.stationIdArgIndex()] instanceof Long l) {
                return l;
            }
        }
        // 직원 엔티티에서 현재 stationId 조회
        if (actorName != null) {
            return staffRepository.findByUsername(actorName)
                    .map(s -> s.getStationId())
                    .orElse(null);
        }
        return null;
    }

    // ─── HTTP 컨텍스트 ─────────────────────────────────────────────────────────

    private String extractIpAddress() {
        try {
            var attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            String forwarded = attrs.getRequest().getHeader("X-Forwarded-For");
            if (forwarded != null && !forwarded.isBlank()) {
                return forwarded.split(",")[0].trim();
            }
            return attrs.getRequest().getRemoteAddr();
        } catch (Exception ignored) {
            return null;
        }
    }

    private String extractUserAgent() {
        try {
            var attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attrs.getRequest().getHeader("User-Agent");
        } catch (Exception ignored) {
            return null;
        }
    }

    // ─── metadata ─────────────────────────────────────────────────────────────

    private String buildMetadata(AuditAction action, Object result, Throwable failure) {
        try {
            Map<String, Object> meta = new LinkedHashMap<>();

            if (failure != null) {
                meta.put("errorType", failure.getClass().getSimpleName());
                return objectMapper.writeValueAsString(meta);
            }

            switch (action) {
                case ORDER_PLACED, ORDER_CONFIRMED, ORDER_COMPLETED, ORDER_CANCELLED -> {
                    Object totalPrice = safeInvoke(result, "getTotalPrice");
                    if (totalPrice != null) meta.put("totalPrice", totalPrice);

                    Object items = safeInvoke(result, "getItems");
                    if (items instanceof List<?> list) meta.put("itemCount", list.size());
                }
                default -> {}
            }

            return meta.isEmpty() ? "{}" : objectMapper.writeValueAsString(meta);
        } catch (Exception e) {
            return "{}";
        }
    }

    // ─── description ──────────────────────────────────────────────────────────

    private String buildDescription(AuditAction action, AuditResult result,
                                    Long entityId, String actorName,
                                    Integer tableNumber, String stationName) {
        String suffix = result == AuditResult.FAILURE ? " (실패)" : "";
        String table = tableNumber != null ? " [테이블 " + tableNumber + "번]" : "";
        String station = stationName != null ? " [" + stationName + "]" : "";
        return switch (action) {
            case ORDER_PLACED -> "주문 #" + entityId + " 접수 (손님)" + table + suffix;
            case ORDER_CONFIRMED -> "주문 #" + entityId + " 확정" + table + station + suffix;
            case ORDER_COMPLETED -> "주문 #" + entityId + " 완료" + table + station + suffix;
            case ORDER_CANCELLED -> "주문 #" + entityId + " 취소" + table + station + suffix;
            case TABLE_OCCUPIED -> "세션 #" + entityId + " 생성 (테이블 점유)" + table + suffix;
            case TABLE_RELEASED -> "세션 #" + entityId + " 종료 (테이블 해제)" + table + suffix;
            case STAFF_LOGIN -> (actorName != null ? actorName : "알 수 없음") + " 로그인" + suffix;
        };
    }

    // ─── 리플렉션 헬퍼 ────────────────────────────────────────────────────────

    private Long extractLong(Object obj, String methodName) {
        Object val = safeInvoke(obj, methodName);
        return val instanceof Long l ? l : null;
    }

    private String extractString(Object obj, String methodName) {
        Object val = safeInvoke(obj, methodName);
        return val instanceof String s ? s : null;
    }

    private Object safeInvoke(Object obj, String methodName) {
        if (obj == null) return null;
        try {
            return obj.getClass().getMethod(methodName).invoke(obj);
        } catch (Exception ignored) {
            return null;
        }
    }
}
