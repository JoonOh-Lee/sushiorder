package com.joonoh.sushiorder.domain.menu.entity;

public enum MenuCategory {
    SUSHI("초밥"),
    ROLL("롤"),
    SIDE("사이드"),
    DRINK("음료"),
    DESSERT("디저트");

    private final String displayName;

    MenuCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
