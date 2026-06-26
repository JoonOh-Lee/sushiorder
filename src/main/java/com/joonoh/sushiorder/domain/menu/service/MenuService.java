package com.joonoh.sushiorder.domain.menu.service;

import com.joonoh.sushiorder.domain.menu.dto.MenuCreateRequest;
import com.joonoh.sushiorder.domain.menu.dto.MenuResponse;
import com.joonoh.sushiorder.domain.menu.dto.MenuSearchCondition;
import com.joonoh.sushiorder.domain.menu.dto.MenuUpdateRequest;
import com.joonoh.sushiorder.domain.menu.entity.Menu;
import com.joonoh.sushiorder.domain.menu.exception.MenuNotFoundException;
import com.joonoh.sushiorder.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MenuService {

    private final MenuRepository menuRepository;

    @Transactional
    public MenuResponse createMenu(MenuCreateRequest request) {
        Menu menu = Menu.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .imageUrl(request.getImageUrl())
                .ingredients(request.getIngredients())
                .allergyInfo(request.getAllergyInfo())
                .stockCount(request.getStockCount())
                .stationId(request.getStationId())
                .build();

        Menu saved = menuRepository.save(menu);
        return MenuResponse.from(menu);
    }

    public MenuResponse getMenu(Long id) {
        return MenuResponse.from(findMenuOrThrow(id));
    }

    private Menu findMenuOrThrow(Long id)  {
        return menuRepository.findById(id)
                .orElseThrow(() -> new MenuNotFoundException(id));
    }

    public List<MenuResponse> searchMenus(MenuSearchCondition condition) {
        return menuRepository.searchMenus(condition) // List<Menu>
                .stream() // Stream<Menu>
                .map(MenuResponse::from) // Stream<MenuResponse>
                .toList(); // List<Menu>
    }

    public List<MenuResponse> getLowStockMenus(int threshold) {
        return menuRepository.findLowStockMenus(threshold) // List<Menu>
                .stream() // Stream<Menu>
                .map(MenuResponse::from) // Stream<MenuResponse>
                .toList(); // List<Menu>
    }

    @Transactional
    public MenuResponse updateMenu(Long id, MenuUpdateRequest request) {
        Menu menu = findMenuOrThrow(id);
        menu.updateInfo(request.getName(), request.getDescription(), request.getCategory(), request.getImageUrl(),
                request.getIngredients(), request.getAllergyInfo());
        return MenuResponse.from(menu);
    }

    @Transactional
    public void changePrice(Long id, int newPrice) {
        findMenuOrThrow(id).changePrice(newPrice);
    }

    @Transactional
    public void restock(Long id, int quantity) {
        findMenuOrThrow(id).restoreStock(quantity);
    }

    @Transactional
    public void setStockCount(Long id, Integer stockCount) {
        findMenuOrThrow(id).setStock(stockCount);
    }

    @Transactional
    public void activateMenu(Long id) {
        findMenuOrThrow(id).activate();
    }

    @Transactional
    public void deactivateMenu(Long id) {
        findMenuOrThrow(id).deactivate();
    }

    @Transactional
    public void like(Long id) {
        findMenuOrThrow(id).increaseLike();
    }
    @Transactional
    public void dislike(Long id) {
        findMenuOrThrow(id).increaseDislike();
    }
}
