package com.joonoh.sushiorder.domain.staffcall.entity;

public enum CallStatus {
    REQUESTED("요청됨"),
    RESOLVED("처리완료");

    private final String displayName;

    CallStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
