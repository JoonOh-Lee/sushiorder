package com.joonoh.sushiorder.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class OrderStatsResponse {
    private LocalDate date;
    private long totalOrders;
    private long totalRevenue;
    private List<MenuStat> topMenus;
    private List<HourlyStat> hourlyDistribution;

    @Getter
    @AllArgsConstructor
    public static class MenuStat {
        private Long menuId;
        private String menuName;
        private long quantity;
        private long revenue;
    }

    @Getter
    @AllArgsConstructor
    public static class HourlyStat {
        private int hour;
        private long orderCount;
    }
}
