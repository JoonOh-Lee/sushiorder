package com.joonoh.sushiorder.domain.auditlog.entity;

public enum AuditAction {
    ORDER_PLACED("주문 접수"),
    ORDER_CONFIRMED("주문 확정"),
    ORDER_COMPLETED("주문 완료"),
    ORDER_CANCELLED("주문 취소"),
    TABLE_OCCUPIED("테이블 점유"),
    TABLE_RELEASED("테이블 해제"),
    STAFF_LOGIN("직원 로그인");

    private final String displayName;

    AuditAction(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
