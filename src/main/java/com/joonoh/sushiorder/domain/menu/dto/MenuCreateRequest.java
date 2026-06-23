package com.joonoh.sushiorder.domain.menu.dto;

import com.joonoh.sushiorder.domain.menu.entity.MenuCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuCreateRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @PositiveOrZero
    private Integer price;

    @NotNull
    private MenuCategory category;

    private String imageUrl;

    private String ingredients; // 원재료 정보

    private String allergyInfo; // 알러지 유발 성분 정보

    private Integer stockCount; // null = 무제한 메뉴

    private Long stationId;
}
