package com.joonoh.sushiorder.domain.menu.repository;

import com.joonoh.sushiorder.domain.menu.dto.MenuSearchCondition;
import com.joonoh.sushiorder.domain.menu.entity.Menu;

import java.util.List;

public interface MenuRepositoryCustom {
    List<Menu> searchMenus(MenuSearchCondition condition);
    List<Menu> findLowStockMenus(int threshhold);
}
