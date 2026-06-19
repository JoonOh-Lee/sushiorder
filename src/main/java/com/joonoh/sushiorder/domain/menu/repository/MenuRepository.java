package com.joonoh.sushiorder.domain.menu.repository;

import com.joonoh.sushiorder.domain.menu.entity.Menu;
import com.joonoh.sushiorder.domain.menu.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByActiveTrue();
    List<Menu> findByCategoryAndActiveTrue(MenuCategory category);
    List<Menu> findByStationId(Long stationId);
}
