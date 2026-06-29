package com.joonoh.sushiorder.domain.restauranttable.service;

import com.joonoh.sushiorder.domain.restauranttable.dto.RestaurantTableCreateRequest;
import com.joonoh.sushiorder.domain.restauranttable.dto.RestaurantTablePositionRequest;
import com.joonoh.sushiorder.domain.restauranttable.dto.RestaurantTableResponse;
import com.joonoh.sushiorder.domain.restauranttable.entity.RestaurantTable;
import com.joonoh.sushiorder.domain.restauranttable.exception.RestaurantTableNotFoundException;
import com.joonoh.sushiorder.domain.restauranttable.repository.RestaurantTableRepository;
import com.joonoh.sushiorder.domain.session.entity.SessionStatus;
import com.joonoh.sushiorder.domain.session.entity.TableSession;
import com.joonoh.sushiorder.domain.session.repository.TableSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;
    private final TableSessionRepository tableSessionRepository;

    @Transactional
    public RestaurantTableResponse createTable(RestaurantTableCreateRequest request) {
        if (restaurantTableRepository.existsBySeatTypeAndTableNumber(request.getSeatType(), request.getTableNumber())) {
            throw new IllegalArgumentException(
                    "이미 존재하는 테이블 번호입니다. seatType=" + request.getSeatType() + ", tableNumber=" + request.getTableNumber());
        }

        RestaurantTable table = RestaurantTable.builder()
                .seatType(request.getSeatType())
                .tableNumber(request.getTableNumber())
                .seatCount(request.getSeatCount())
                .build();

        return RestaurantTableResponse.from(restaurantTableRepository.save(table));
    }

    public List<RestaurantTableResponse> getAllTables() {
        return restaurantTableRepository.findAll().stream()
                .map(RestaurantTableResponse::from)
                .toList();
    }

    public RestaurantTableResponse getTable(Long id) {
        return RestaurantTableResponse.from(findTableOrThrow(id));
    }

    /** 직원이 워크인 손님을 수동으로 착석 처리 (QR 세션 없이) */
    @Transactional
    public void occupyTable(Long id) {
        findTableOrThrow(id).occupy();
    }

    /** 계산 완료 후 자리 비움 — 연결된 활성 세션도 함께 CLOSED */
    @Transactional
    public void releaseTable(Long id) {
        findTableOrThrow(id).release();
        tableSessionRepository.findByTableIdAndStatus(id, SessionStatus.ACTIVE)
                .ifPresent(TableSession::close);
    }

    @Transactional
    public void reserveTable(Long id) {
        findTableOrThrow(id).reserve();
    }

    @Transactional
    public void cancelReservation(Long id) {
        findTableOrThrow(id).cancelReservation();
    }

    /** 매장 평면도 위치/크기 수정 — ADMIN 전용 */
    @Transactional
    public RestaurantTableResponse updatePosition(Long id, RestaurantTablePositionRequest request) {
        RestaurantTable table = findTableOrThrow(id);
        table.updatePosition(request.getX(), request.getY(), request.getWidth(), request.getHeight());
        return RestaurantTableResponse.from(table);
    }

    private RestaurantTable findTableOrThrow(Long id) {
        return restaurantTableRepository.findById(id)
                .orElseThrow(() -> new RestaurantTableNotFoundException(id));
    }
}
