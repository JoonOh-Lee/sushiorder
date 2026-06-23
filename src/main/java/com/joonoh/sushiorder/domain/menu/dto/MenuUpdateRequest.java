package com.joonoh.sushiorder.domain.menu.dto;

import com.joonoh.sushiorder.domain.menu.entity.MenuCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuUpdateRequest {

    @NotBlank
    private String name;

    private String description;

    private MenuCategory category;

    private String imageUrl;

    private String ingredients; // 원재료 정보

    private String allergyInfo; // 알러지 유발 성분 정보
}
