package com.joonoh.sushiorder.domain.menu.service;

import com.joonoh.sushiorder.domain.menu.dto.MenuCreateRequest;
import com.joonoh.sushiorder.domain.menu.dto.MenuResponse;
import com.joonoh.sushiorder.domain.menu.dto.MenuSearchCondition;
import com.joonoh.sushiorder.domain.menu.dto.MenuUpdateRequest;
import com.joonoh.sushiorder.domain.menu.entity.MenuCategory;
import com.joonoh.sushiorder.domain.menu.exception.MenuNotFoundException;
import com.joonoh.sushiorder.domain.menu.repository.MenuRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MenuServiceTest {

    @Autowired private MenuService menuService;
    @Autowired private MenuRepository menuRepository;

    private Long menuId;

    @AfterEach
    void tearDown() {
        if (menuId != null) {
            menuRepository.deleteById(menuId);
            menuId = null;
        }
    }

    @Test
    @DisplayName("메뉴를 생성하면 판매중 상태로 시작하고 좋아요/싫어요는 0이다")
    void createMenu_startsActiveWithZeroCounts() {
        MenuResponse response = menuService.createMenu(createRequest("연어초밥", 3000, 10));
        menuId = response.getId();

        assertThat(response.isActive()).isTrue();
        assertThat(response.getLikeCount()).isZero();
        assertThat(response.getDislikeCount()).isZero();
        assertThat(response.getStockCount()).isEqualTo(10);
        assertThat(response.isLimitedStock()).isTrue();
    }

    @Test
    @DisplayName("재고 수량이 null이면 무제한 메뉴다")
    void createMenu_nullStock_isUnlimited() {
        MenuResponse response = menuService.createMenu(createRequest("연어초밥", 3000, null));
        menuId = response.getId();

        assertThat(response.isLimitedStock()).isFalse();
        assertThat(response.isSoldOut()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 메뉴는 조회할 수 없다")
    void getMenu_notFound_throws() {
        assertThatThrownBy(() -> menuService.getMenu(999_999L))
                .isInstanceOf(MenuNotFoundException.class);
    }

    @Test
    @DisplayName("가격을 변경하면 반영된다")
    void changePrice_updatesPrice() {
        MenuResponse created = menuService.createMenu(createRequest("연어초밥", 3000, 10));
        menuId = created.getId();

        menuService.changePrice(menuId, 3500);

        assertThat(menuService.getMenu(menuId).getPrice()).isEqualTo(3500);
    }

    @Test
    @DisplayName("음수 가격으로는 변경할 수 없다")
    void changePrice_negative_throws() {
        MenuResponse created = menuService.createMenu(createRequest("연어초밥", 3000, 10));
        menuId = created.getId();

        assertThatThrownBy(() -> menuService.changePrice(menuId, -100))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("재입고하면 재고가 늘어난다")
    void restock_increasesStock() {
        MenuResponse created = menuService.createMenu(createRequest("연어초밥", 3000, 5));
        menuId = created.getId();

        menuService.restock(menuId, 3);

        assertThat(menuService.getMenu(menuId).getStockCount()).isEqualTo(8);
    }

    @Test
    @DisplayName("재고를 null로 설정하면 무제한 메뉴로 전환된다")
    void setStockCount_null_becomesUnlimited() {
        MenuResponse created = menuService.createMenu(createRequest("연어초밥", 3000, 5));
        menuId = created.getId();

        menuService.setStockCount(menuId, null);

        assertThat(menuService.getMenu(menuId).isLimitedStock()).isFalse();
    }

    @Test
    @DisplayName("비활성화 후 재활성화하면 판매 상태가 오간다")
    void deactivateThenActivate_togglesStatus() {
        MenuResponse created = menuService.createMenu(createRequest("연어초밥", 3000, 5));
        menuId = created.getId();

        menuService.deactivateMenu(menuId);
        assertThat(menuService.getMenu(menuId).isActive()).isFalse();

        menuService.activateMenu(menuId);
        assertThat(menuService.getMenu(menuId).isActive()).isTrue();
    }

    @Test
    @DisplayName("좋아요/싫어요를 누르면 카운트가 오른다")
    void likeAndDislike_incrementCounts() {
        MenuResponse created = menuService.createMenu(createRequest("연어초밥", 3000, 5));
        menuId = created.getId();

        menuService.like(menuId);
        menuService.like(menuId);
        menuService.dislike(menuId);

        MenuResponse updated = menuService.getMenu(menuId);
        assertThat(updated.getLikeCount()).isEqualTo(2);
        assertThat(updated.getDislikeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("메뉴 정보를 수정하면 이름/설명이 바뀐다")
    void updateMenu_changesNameAndDescription() {
        MenuResponse created = menuService.createMenu(createRequest("연어초밥", 3000, 5));
        menuId = created.getId();

        MenuUpdateRequest request = new MenuUpdateRequest();
        setField(request, "name", "새 연어초밥");
        setField(request, "description", "새로 온 노르웨이산 연어");
        setField(request, "category", MenuCategory.FRESH_SUSHI);

        menuService.updateMenu(menuId, request);

        MenuResponse updated = menuService.getMenu(menuId);
        assertThat(updated.getName()).isEqualTo("새 연어초밥");
        assertThat(updated.getDescription()).isEqualTo("새로 온 노르웨이산 연어");
    }

    @Test
    @DisplayName("이름에 키워드가 포함된 메뉴만 검색된다")
    void searchMenus_byKeyword_filtersByName() {
        MenuResponse created = menuService.createMenu(createRequest("특급참치초밥", 5000, 5));
        menuId = created.getId();

        MenuSearchCondition condition = MenuSearchCondition.builder().keyword("특급참치").build();

        assertThat(menuService.searchMenus(condition))
                .extracting(MenuResponse::getId)
                .contains(menuId);
    }

    @Test
    @DisplayName("재고가 임계값 이하인 판매중 메뉴만 조회된다")
    void getLowStockMenus_returnsMenusAtOrBelowThreshold() {
        MenuResponse created = menuService.createMenu(createRequest("연어초밥", 3000, 2));
        menuId = created.getId();

        assertThat(menuService.getLowStockMenus(2))
                .extracting(MenuResponse::getId)
                .contains(menuId);
        assertThat(menuService.getLowStockMenus(1))
                .extracting(MenuResponse::getId)
                .doesNotContain(menuId);
    }

    private MenuCreateRequest createRequest(String name, int price, Integer stockCount) {
        MenuCreateRequest request = new MenuCreateRequest();
        setField(request, "name", name);
        setField(request, "price", price);
        setField(request, "category", MenuCategory.FRESH_SUSHI);
        setField(request, "stockCount", stockCount);
        return request;
    }

    private void setField(Object target, String name, Object value) {
        try {
            var field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
