package com.joonoh.sushiorder.domain.order.dto;

import com.joonoh.sushiorder.domain.order.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemResponse {
    private Long id;
    private Long menuId;
    private String menuName;
    private int unitPrice;
    private int quantity;
    private int subtotal;

    public static OrderItemResponse from(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .menuId(item.getMenuId())
                .menuName(item.getMenuName())
                .unitPrice(item.getUnitPrice())
                .quantity(item.getQuantity())
                .subtotal(item.getSubtotal())
                .build();
    }
}