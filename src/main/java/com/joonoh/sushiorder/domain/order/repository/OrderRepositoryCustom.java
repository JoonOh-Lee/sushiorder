package com.joonoh.sushiorder.domain.order.repository;

import com.joonoh.sushiorder.domain.order.dto.OrderSearchCondition;
import com.joonoh.sushiorder.domain.order.entity.Order;
import com.joonoh.sushiorder.domain.order.entity.OrderStatus;

import java.util.List;

public interface OrderRepositoryCustom {
    /** 특정 테이블의 진행 중인 주문들 (PENDING + CONFIRMED) */
    List<Order> findActiveOrdersByTableId(Long tableId);

    /** 특정 세션의 모든 주문 (손님 입장에서 본인 주문 내역) */
    List<Order> findBySessionId(Long sessionId);

    /** 상태별 주문 조회 (주방용: CONFIRMED 주문만 뽑기) */
    List<Order> findByStatus(OrderStatus status);

    /** 상태 + station별 주문 조회 — 해당 station이 담당하는 메뉴가 하나라도 포함된 주문 (station 화면용) */
    List<Order> findByStatusAndStationId(OrderStatus status, Long stationId);

    /** 동적 조건 검색 (관리자용) */
    List<Order> searchOrders(OrderSearchCondition condition);
}
