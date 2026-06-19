package com.joonoh.sushiorder.domain.menu.dto;

import com.joonoh.sushiorder.domain.menu.entity.Menu;
import com.joonoh.sushiorder.domain.menu.entity.MenuCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuResponse {
    private Long id;
    private String name;
    private String description;
    private int price;
    private MenuCategory category;
    private String imageUrl;
    private Integer stockCount;
    private boolean limitedStock;
    private int likeCount;
    private int dislikeCount;
    private boolean active;
    private Long stationId;

    public static MenuResponse from(Menu menu) {
        return MenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .category(menu.getCategory())
                .imageUrl(menu.getImageUrl())
                .stockCount(menu.getStockCount())
                .limitedStock(menu.isLimitedStock())
                .likeCount(menu.getLikeCount())
                .dislikeCount(menu.getDislikeCount())
                .active(menu.isActive())
                .stationId(menu.getStationId())
                .build();
    }
}
