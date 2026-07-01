package com.joonoh.sushiorder.domain.menu.controller;

import com.joonoh.sushiorder.domain.menu.dto.*;
import com.joonoh.sushiorder.domain.menu.entity.MenuCategory;
import com.joonoh.sushiorder.domain.menu.service.MenuService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/menu")
@RequiredArgsConstructor
public class AdminMenuController {

    private final MenuService menuService;

    @GetMapping("/low-stock")
    public ApiResponse<List<MenuResponse>> getLowStockMenus(@RequestParam(defaultValue = "5") int threshold) {
        return ApiResponse.success(menuService.getLowStockMenus(threshold));
    }

    @PostMapping
    public ApiResponse<MenuResponse> createMenu(@Valid @RequestBody MenuCreateRequest request) {
        return ApiResponse.success(menuService.createMenu(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<MenuResponse> updateMenu(@PathVariable Long id,
                                                @Valid @RequestBody MenuUpdateRequest request) {
        return ApiResponse.success(menuService.updateMenu(id, request));
    }
    @PatchMapping("/{id}/price")
    public ApiResponse<Void> changePrice(@PathVariable Long id,
                                         @Valid @RequestBody PriceChangeRequest request) {
        menuService.changePrice(id, request.getPrice());
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/restock")
    public ApiResponse<Void> restock(@PathVariable Long id,
                                     @Valid @RequestBody RestockRequest request) {
        menuService.restock(id, request.getQuantity());
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/stock")
    public ApiResponse<Void> setStock(@PathVariable Long id,
                                      @Valid @RequestBody StockSetRequest request) {
        menuService.setStockCount(id, request.getStockCount());
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/activate")
    public ApiResponse<Void> activateMenu(@PathVariable Long id) {
        menuService.activateMenu(id);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/deactivate")
    public ApiResponse<Void> deactivateMenu(@PathVariable Long id) {
        menuService.deactivateMenu(id);
        return ApiResponse.success();
    }

}
