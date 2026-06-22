package com.joonoh.sushiorder.domain.restauranttable.entity;

public enum TableStatus {
    EMPTY("빈 자리"),
    OCCUPIED("사용 중"),
    RESERVED("예약됨");

    private final String displayName;

    TableStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}