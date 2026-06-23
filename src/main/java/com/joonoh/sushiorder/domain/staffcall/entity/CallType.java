package com.joonoh.sushiorder.domain.staffcall.entity;

public enum CallType {
    WATER_REFILL("물 리필"),
    INQUIRY("문의"),
    OTHER("기타");

    private final String displayName;

    CallType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
