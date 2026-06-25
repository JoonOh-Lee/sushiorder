package com.joonoh.sushiorder.domain.floorplan.entity;

public enum FloorPlanElementType {
    KITCHEN("주방"),
    RAIL("회전초밥 레일"),
    ETC("기타");

    private final String displayName;

    FloorPlanElementType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
