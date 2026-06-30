package com.joonoh.sushiorder.global.audit;

import com.joonoh.sushiorder.domain.auditlog.entity.AuditAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {
    AuditAction action();
    String entityType();

    /**
     * 메서드 인자 중 tableId가 위치한 인덱스 (0부터).
     * -1(기본값)이면 리턴값의 getTableId()에서 추출.
     */
    int tableIdArgIndex() default -1;

    /**
     * 메서드 인자 중 stationId가 위치한 인덱스 (0부터).
     * -1(기본값)이면 SecurityContext 인증 직원의 stationId를 DB 조회.
     */
    int stationIdArgIndex() default -1;
}
