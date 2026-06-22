package com.joonoh.sushiorder.domain.order.dto;

import com.joonoh.sushiorder.domain.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderSearchCondition {
    private Long tableId;
    private OrderStatus status;
    private LocalDateTime from;
    private LocalDateTime to;
}