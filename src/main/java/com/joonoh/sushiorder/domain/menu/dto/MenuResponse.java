package com.joonoh.sushiorder.domain.menu.dto;

import com.joonoh.sushiorder.domain.menu.entity.Menu;
import com.joonoh.sushiorder.domain.menu.entity.MenuCategory;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Getter
@Builder
public class MenuResponse {
    private static final String NODATA_IMAGE_PATH = "/images/nodata.svg";

    private Long id;
    private String name;
    private String description;
    private int price;
    private MenuCategory category;
    private String imageUrl;
    private String ingredients;
    private String allergyInfo;
    private Integer stockCount;
    private boolean limitedStock;
    private boolean soldOut;
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
                .imageUrl(toAbsoluteUrl(menu.getImageUrl()))
                .ingredients(menu.getIngredients())
                .allergyInfo(menu.getAllergyInfo())
                .stockCount(menu.getStockCount())
                .limitedStock(menu.isLimitedStock())
                .soldOut(menu.isLimitedStock() && menu.getStockCount() == 0)
                .likeCount(menu.getLikeCount())
                .dislikeCount(menu.getDislikeCount())
                .active(menu.isActive())
                .stationId(menu.getStationId())
                .build();
    }

    private static String toAbsoluteUrl(String imageUrl) {
        String path = (imageUrl == null || imageUrl.isBlank()) ? NODATA_IMAGE_PATH : imageUrl;
        if (!path.startsWith("/")) {
            return path;
        }
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path)
                .toUriString();
    }
}
