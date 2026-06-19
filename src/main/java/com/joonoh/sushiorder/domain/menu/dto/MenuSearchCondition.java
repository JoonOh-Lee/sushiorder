package com.joonoh.sushiorder.domain.menu.dto;

import com.joonoh.sushiorder.domain.menu.entity.MenuCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuSearchCondition {
   private MenuCategory menuCategory;
   private String keyword;
   private Long stationId;
   private Boolean activeOnly;
}
