package com.joonoh.sushiorder.domain.order.dto;

import com.joonoh.sushiorder.domain.order.entity.Order;
import com.joonoh.sushiorder.domain.order.entity.OrderStatus;
import com.joonoh.sushiorder.domain.restauranttable.entity.RestaurantTable;
import com.joonoh.sushiorder.domain.restauranttable.entity.SeatType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderResponse {
    private Long id;
    private Long tableId;
    private SeatType seatType;
    private int tableNumber;
    private Long sessionId;
    private OrderStatus status;
    private int totalPrice;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;

    /**
     * 직원 화면에서 "테이블 3번" 처럼 바로 표시할 수 있도록 RestaurantTable 정보를 같이 채운다.
     * table이 null이면(테이블 조회 실패) seatType/tableNumber만 비워두고 — 주문 처리 자체를 막지는 않는다.
     */
    public static OrderResponse from(Order order, RestaurantTable table) {
        return OrderResponse.builder()
                .id(order.getId())
                .tableId(order.getTableId())
                .seatType(table != null ? table.getSeatType() : null)
                .tableNumber(table != null ? table.getTableNumber() : 0)
                .sessionId(order.getSessionId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .items(order.getItems().stream()
                        .map(OrderItemResponse::from)
                        .toList())
                .createdAt(order.getCreatedAt())
                .build();
    }
}