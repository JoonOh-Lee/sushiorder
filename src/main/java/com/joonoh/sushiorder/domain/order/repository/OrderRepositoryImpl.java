package com.joonoh.sushiorder.domain.order.repository;

import com.joonoh.sushiorder.domain.order.dto.OrderSearchCondition;
import com.joonoh.sushiorder.domain.order.entity.Order;
import com.joonoh.sushiorder.domain.order.entity.OrderStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.joonoh.sushiorder.domain.menu.entity.QMenu.menu;
import static com.joonoh.sushiorder.domain.order.entity.QOrder.order;
import static com.joonoh.sushiorder.domain.order.entity.QOrderItem.orderItem;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Order> findActiveOrdersByTableId(Long tableId) {
        return queryFactory
                .selectFrom(order)
                .leftJoin(order.items, orderItem).fetchJoin()  // ← 추가
                .where(
                        order.tableId.eq(tableId),
                        order.status.in(OrderStatus.PENDING, OrderStatus.CONFIRMED)
                )
                .orderBy(order.id.asc())
                .distinct()  // ← items가 여러 개면 order가 중복으로 나오니 distinct
                .fetch();
    }
    @Override
    public List<Order> findBySessionId(Long sessionId) {
        return queryFactory
                .selectFrom(order)
                .leftJoin(order.items, orderItem).fetchJoin()
                .where(order.sessionId.eq(sessionId))
                .orderBy(order.id.desc())
                .distinct()
                .fetch();
    }

    @Override
    public List<Order> findByStatusIn(List<OrderStatus> statuses) {
        return queryFactory
                .selectFrom(order)
                .leftJoin(order.items, orderItem).fetchJoin()
                .where(order.status.in(statuses))
                .orderBy(order.id.asc())
                .distinct()
                .fetch();
    }

    @Override
    public List<Order> findByStatusInAndStationId(List<OrderStatus> statuses, Long stationId) {
        return queryFactory
                .selectFrom(order)
                .leftJoin(order.items, orderItem).fetchJoin()
                .where(
                        order.status.in(statuses),
                        order.id.in(
                                JPAExpressions.select(orderItem.order.id)
                                        .from(orderItem)
                                        .join(menu).on(orderItem.menuId.eq(menu.id))
                                        .where(menu.stationId.eq(stationId))
                        )
                )
                .orderBy(order.id.asc())
                .distinct()
                .fetch();
    }

    @Override
    public List<Order> searchOrders(OrderSearchCondition condition) {
        return queryFactory
                .selectFrom(order)
                .where(
                        tableIdEq(condition.getTableId()),
                        statusEq(condition.getStatus()),
                        createdAfter(condition.getFrom()),
                        createdBefore(condition.getTo())
                )
                .orderBy(order.id.desc())
                .fetch();
    }

    private BooleanExpression tableIdEq(Long tableId) {
        return tableId != null ? order.tableId.eq(tableId) : null;
    }

    private BooleanExpression statusEq(OrderStatus status) {
        return status != null ? order.status.eq(status) : null;
    }

    private BooleanExpression createdAfter(LocalDateTime from) {
        return from != null ? order.createdAt.goe(from) : null;
    }

    private BooleanExpression createdBefore(LocalDateTime to) {
        return to != null ? order.createdAt.loe(to) : null;
    }
}