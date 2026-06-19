package com.joonoh.sushiorder.domain.menu.controller;

import com.joonoh.sushiorder.domain.menu.dto.*;
import com.joonoh.sushiorder.domain.menu.entity.MenuCategory;
import com.joonoh.sushiorder.domain.menu.service.MenuService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;


    @GetMapping
    public ApiResponse<List<MenuResponse>> searchMenus(
            @RequestParam(required = false) MenuCategory category,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long stationId,
            @RequestParam(required = false) Boolean activeOnly) {
        MenuSearchCondition condition = MenuSearchCondition.builder()
                .category(category)
                .keyword(keyword)
                .stationId(stationId)
                .activeOnly(activeOnly)
                .build();
        return ApiResponse.success(menuService.searchMenus(condition));
    }

    @GetMapping("/{id}")
    public ApiResponse<MenuResponse> getMenu(@PathVariable Long id) {
        return ApiResponse.success(menuService.getMenu(id));
    }

    @PostMapping("/{id}/like")
    public ApiResponse<Void> like(@PathVariable Long id) {
        menuService.like(id);
        return ApiResponse.success();
    }

    @PostMapping("/{id}/dislike")
    public ApiResponse<Void> dislike(@PathVariable Long id) {
        menuService.dislike(id);
        return ApiResponse.success();
    }
}
