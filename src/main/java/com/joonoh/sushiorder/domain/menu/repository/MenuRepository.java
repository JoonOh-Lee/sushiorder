package com.joonoh.sushiorder.domain.menu.repository;

import com.joonoh.sushiorder.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

}
