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
}
