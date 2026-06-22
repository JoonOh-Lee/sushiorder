package com.joonoh.sushiorder.domain.order.repository;

import com.joonoh.sushiorder.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    /** Idempotency Key로 기존 주문 조회 (중복주문방지)*/
    Optional<Order> findByIdempotencyKey(String idempotencyKey);
}
