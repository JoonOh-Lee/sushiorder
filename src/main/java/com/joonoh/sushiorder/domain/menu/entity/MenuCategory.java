package com.joonoh.sushiorder.domain.menu.entity;

public enum MenuCategory {
    PREMIUM_SUSHI("프리미엄초밥"),
    FRESH_SUSHI("신선초밥"),
    TUNA_SUSHI("참치초밥"),
    MEAT_SUSHI("고기초밥"),
    GRILLED_SUSHI("구운초밥"),
    SEASONED_SUSHI("양념초밥"),
    GUNKAN_SUSHI("군함초밥"),
    FRIED("튀김류"),
    DESSERT_ETC("디저트/기타"),
    MEAL("식사류"),
    DRINK_ALCOHOL("음료/주류"),
    TAKEOUT("포장");

    private final String displayName;

    MenuCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
