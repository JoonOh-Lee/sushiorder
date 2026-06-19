package com.joonoh.sushiorder.domain.menu.repository;

import com.joonoh.sushiorder.domain.menu.dto.MenuSearchCondition;
import com.joonoh.sushiorder.domain.menu.entity.Menu;
import com.joonoh.sushiorder.domain.menu.entity.MenuCategory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.joonoh.sushiorder.domain.menu.entity.QMenu.menu;

@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public List<Menu> searchMenus(MenuSearchCondition condition) {
        return queryFactory
                .selectFrom(menu)
                .where(
                        categoryEq(condition.getMenuCategory()),
                        keywordContains(condition.getKeyword()),
                        stationIdEq(condition.getStationId()),
                        activeEq(condition.getActiveOnly())
                ).orderBy(menu.category.asc(), menu.id.asc())
                .fetch();
    }

    private BooleanExpression categoryEq(MenuCategory category) {
        return category != null ? menu.category.eq(category) : null;
    }

    private BooleanExpression keywordContains(String keyword) {
        return StringUtils.hasText(keyword) ? menu.name.containsIgnoreCase(keyword) : null;
    }

    private BooleanExpression stationIdEq(Long stationId) {
        return stationId != null ? menu.stationId.eq(stationId) : null;
    }

    private BooleanExpression activeEq(Boolean activeOnly) {
        return Boolean.TRUE.equals(activeOnly) ? menu.active.isTrue() : null;
    }
    @Override
    public List<Menu> findLowStockMenus(int threshhold) {
        return queryFactory
                .selectFrom(menu)
                .where(
                        menu.stockCount.isNotNull(),
                        menu.stockCount.loe(threshhold),
                        menu.active.isTrue()
                ).orderBy(menu.stockCount.asc())
                .fetch();
    }
}
