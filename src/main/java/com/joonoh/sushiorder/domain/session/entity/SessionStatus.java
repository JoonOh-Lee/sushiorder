package com.joonoh.sushiorder.domain.session.entity;

public enum SessionStatus {
    ACTIVE("활성"),
    EXPIRED("만료됨"),
    CLOSED("종료됨");

    private final String displayName;

    SessionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
