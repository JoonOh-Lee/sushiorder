package com.joonoh.sushiorder.domain.staff.entity;

public enum StaffRole {
    ADMIN("관리자"),
    STAFF("직원");

    private final String displayName;

    StaffRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
