package com.joonoh.sushiorder.domain.session.service;

import com.joonoh.sushiorder.domain.restauranttable.entity.RestaurantTable;
import com.joonoh.sushiorder.domain.restauranttable.exception.RestaurantTableNotFoundException;
import com.joonoh.sushiorder.domain.restauranttable.repository.RestaurantTableRepository;
import com.joonoh.sushiorder.domain.session.dto.TableSessionResponse;
import com.joonoh.sushiorder.domain.session.entity.SessionStatus;
import com.joonoh.sushiorder.domain.session.entity.TableSession;
import com.joonoh.sushiorder.domain.session.exception.TableSessionNotFoundException;
import com.joonoh.sushiorder.domain.session.repository.TableSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TableSessionService {

    private static final Duration DEFAULT_TTL = Duration.ofHours(2);

    private final TableSessionRepository tableSessionRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    /**
     * QR 스캔 → 세션 시작.
     * 같은 테이블에 활성 세션이 있으면 막고, 테이블도 OCCUPIED로 전환한다.
     */
    @Transactional
    public TableSessionResponse createSession(Long tableId) {
        tableSessionRepository.findByTableIdAndStatus(tableId, SessionStatus.ACTIVE)
                .ifPresent(existing -> {
                    throw new IllegalStateException(
                            String.format("이미 활성 세션이 있는 테이블입니다. tableId=%d, sessionId=%d", tableId, existing.getId()));
                });

        RestaurantTable table = findTableOrThrow(tableId);
        table.occupy();

        TableSession session = TableSession.builder()
                .tableId(tableId)
                .ttl(DEFAULT_TTL)
                .build();
        TableSession saved = tableSessionRepository.save(session);

        log.info("세션 생성 — sessionId={}, tableId={}", saved.getId(), tableId);
        return TableSessionResponse.from(saved);
    }

    /** 직원 → 테이블 정리/퇴실 처리 */
    @Transactional
    public TableSessionResponse closeSession(Long sessionId) {
        TableSession session = findSessionOrThrow(sessionId);
        session.close();
        releaseTable(session.getTableId());

        log.info("세션 종료 — sessionId={}, tableId={}", sessionId, session.getTableId());
        return TableSessionResponse.from(session);
    }

    /** 만료 스케줄러가 주기적으로 호출 — 기한이 지난 활성 세션을 일괄 만료 처리 */
    @Transactional
    public void expireOverdueSessions() {
        List<TableSession> overdue =
                tableSessionRepository.findAllByStatusAndExpiresAtBefore(SessionStatus.ACTIVE, LocalDateTime.now());

        for (TableSession session : overdue) {
            session.expire();
            releaseTable(session.getTableId());
            log.info("세션 만료 — sessionId={}, tableId={}", session.getId(), session.getTableId());
        }
    }

    private void releaseTable(Long tableId) {
        findTableOrThrow(tableId).release();
    }

    // ===== 조회 메서드 =====

    public TableSessionResponse getSession(Long sessionId) {
        return TableSessionResponse.from(findSessionOrThrow(sessionId));
    }

    /** QR 인증 인터셉터가 토큰으로 세션을 조회할 때 사용. 유효성(isUsable) 판단은 호출 측 책임. */
    public TableSessionResponse getSessionByToken(String sessionToken) {
        return TableSessionResponse.from(findSessionByTokenOrThrow(sessionToken));
    }

    private TableSession findSessionOrThrow(Long sessionId) {
        return tableSessionRepository.findById(sessionId)
                .orElseThrow(() -> new TableSessionNotFoundException(sessionId));
    }

    private TableSession findSessionByTokenOrThrow(String sessionToken) {
        return tableSessionRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new TableSessionNotFoundException(sessionToken));
    }

    private RestaurantTable findTableOrThrow(Long tableId) {
        return restaurantTableRepository.findById(tableId)
                .orElseThrow(() -> new RestaurantTableNotFoundException(tableId));
    }
}
