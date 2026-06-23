package com.joonoh.sushiorder.domain.restauranttable.entity;

public enum SeatType {
    TABLE("테이블"),
    COUNTER("다찌석");

    private final String displayName;

    SeatType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
