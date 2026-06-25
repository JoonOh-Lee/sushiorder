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

    /**
     * station 화면용 — 주문 전체 status가 아니라 "그 station이 담당하는 item 자체의 status"로 필터링.
     * 같은 주문에 다른 station 메뉴가 섞여 있어도, 이 station 담당 item이 statuses에 해당하면 그 주문을 보여준다
     * (item별 접수/완료/취소는 OrderItem.status로, OrderItem.stationId는 주문 시점 메뉴 station 스냅샷).
     */
    @Override
    public List<Order> findByStatusInAndStationId(List<OrderStatus> statuses, Long stationId) {
        return queryFactory
                .selectFrom(order)
                .leftJoin(order.items, orderItem).fetchJoin()
                .where(
                        order.id.in(
                                JPAExpressions.select(orderItem.order.id)
                                        .from(orderItem)
                                        .where(
                                                orderItem.stationId.eq(stationId),
                                                orderItem.status.in(statuses)
                                        )
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