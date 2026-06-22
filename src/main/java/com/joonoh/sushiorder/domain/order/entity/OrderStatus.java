package com.joonoh.sushiorder.domain.order.entity;

public enum OrderStatus {
    PENDING("주문 접수"),
    CONFIRMED("조리 중"),
    COMPLETED("서빙 완료"),
    CANCELLED("취소됨");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
