package com.joonoh.sushiorder.domain.session.service;

import com.joonoh.sushiorder.domain.restauranttable.entity.RestaurantTable;
import com.joonoh.sushiorder.domain.restauranttable.entity.SeatType;
import com.joonoh.sushiorder.domain.restauranttable.entity.TableStatus;
import com.joonoh.sushiorder.domain.restauranttable.exception.RestaurantTableNotFoundException;
import com.joonoh.sushiorder.domain.restauranttable.repository.RestaurantTableRepository;
import com.joonoh.sushiorder.domain.session.dto.TableSessionResponse;
import com.joonoh.sushiorder.domain.session.entity.SessionStatus;
import com.joonoh.sushiorder.domain.session.entity.TableSession;
import com.joonoh.sushiorder.domain.session.exception.TableSessionNotFoundException;
import com.joonoh.sushiorder.domain.session.repository.TableSessionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TableSessionServiceTest {

    @Autowired private TableSessionService tableSessionService;
    @Autowired private TableSessionRepository tableSessionRepository;
    @Autowired private RestaurantTableRepository restaurantTableRepository;

    private Long tableId;

    @BeforeEach
    void setUp() {
        RestaurantTable table = RestaurantTable.builder()
                .seatType(SeatType.TABLE)
                .tableNumber(9001)
                .seatCount(4)
                .build();
        tableId = restaurantTableRepository.saveAndFlush(table).getId();
    }

    @AfterEach
    void tearDown() {
        tableSessionRepository.deleteAll();
        restaurantTableRepository.deleteById(tableId);
    }

    @Test
    @DisplayName("세션 생성 시 테이블도 OCCUPIED로 전환된다")
    void createSession_occupiesTable() {
        TableSessionResponse response = tableSessionService.createSession(tableId);

        assertThat(response.getStatus()).isEqualTo(SessionStatus.ACTIVE);
        assertThat(response.getTableId()).isEqualTo(tableId);
        assertThat(response.getSessionToken()).isNotBlank();

        RestaurantTable table = restaurantTableRepository.findById(tableId).orElseThrow();
        assertThat(table.getStatus()).isEqualTo(TableStatus.OCCUPIED);
    }

    @Test
    @DisplayName("이미 활성 세션이 있는 테이블에는 새 세션을 만들 수 없다")
    void createSession_duplicateActiveSession_throws() {
        tableSessionService.createSession(tableId);

        assertThatThrownBy(() -> tableSessionService.createSession(tableId))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("존재하지 않는 테이블로는 세션을 만들 수 없다")
    void createSession_tableNotFound_throws() {
        assertThatThrownBy(() -> tableSessionService.createSession(999_999L))
                .isInstanceOf(RestaurantTableNotFoundException.class);
    }

    @Test
    @DisplayName("세션을 종료하면 CLOSED 상태가 되고 테이블도 비워진다")
    void closeSession_releasesTable() {
        TableSessionResponse created = tableSessionService.createSession(tableId);

        TableSessionResponse closed = tableSessionService.closeSession(created.getId());

        assertThat(closed.getStatus()).isEqualTo(SessionStatus.CLOSED);
        assertThat(closed.getEndedAt()).isNotNull();

        RestaurantTable table = restaurantTableRepository.findById(tableId).orElseThrow();
        assertThat(table.getStatus()).isEqualTo(TableStatus.EMPTY);
    }

    @Test
    @DisplayName("존재하지 않는 세션은 종료할 수 없다")
    void closeSession_notFound_throws() {
        assertThatThrownBy(() -> tableSessionService.closeSession(999_999L))
                .isInstanceOf(TableSessionNotFoundException.class);
    }

    @Test
    @DisplayName("이미 종료된 세션은 다시 종료할 수 없다")
    void closeSession_alreadyClosed_throws() {
        TableSessionResponse created = tableSessionService.createSession(tableId);
        tableSessionService.closeSession(created.getId());

        assertThatThrownBy(() -> tableSessionService.closeSession(created.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("만료 스케줄러는 기한이 지난 활성 세션만 EXPIRED로 바꾸고 테이블을 비운다")
    void expireOverdueSessions_onlyExpiresOverdueActiveSessions() {
        TableSessionResponse overdue = tableSessionService.createSession(tableId);
        backdateExpiresAt(overdue.getId(), LocalDateTime.now().minusMinutes(1));

        RestaurantTable anotherTable = restaurantTableRepository.saveAndFlush(
                RestaurantTable.builder().seatType(SeatType.TABLE).tableNumber(9002).seatCount(2).build());
        TableSessionResponse stillActive = tableSessionService.createSession(anotherTable.getId());

        try {
            tableSessionService.expireOverdueSessions();

            TableSession overdueSession = tableSessionRepository.findById(overdue.getId()).orElseThrow();
            assertThat(overdueSession.getStatus()).isEqualTo(SessionStatus.EXPIRED);
            assertThat(restaurantTableRepository.findById(tableId).orElseThrow().getStatus())
                    .isEqualTo(TableStatus.EMPTY);

            TableSession activeSession = tableSessionRepository.findById(stillActive.getId()).orElseThrow();
            assertThat(activeSession.getStatus()).isEqualTo(SessionStatus.ACTIVE);
            assertThat(restaurantTableRepository.findById(anotherTable.getId()).orElseThrow().getStatus())
                    .isEqualTo(TableStatus.OCCUPIED);
        } finally {
            restaurantTableRepository.deleteById(anotherTable.getId());
        }
    }

    @Test
    @DisplayName("토큰으로 세션을 조회할 수 있다")
    void getSessionByToken_success() {
        TableSessionResponse created = tableSessionService.createSession(tableId);

        TableSessionResponse found = tableSessionService.getSessionByToken(created.getSessionToken());

        assertThat(found.getId()).isEqualTo(created.getId());
    }

    @Test
    @DisplayName("존재하지 않는 토큰으로 조회하면 예외가 발생한다")
    void getSessionByToken_notFound_throws() {
        assertThatThrownBy(() -> tableSessionService.getSessionByToken("no-such-token"))
                .isInstanceOf(TableSessionNotFoundException.class);
    }

    private void backdateExpiresAt(Long sessionId, LocalDateTime expiresAt) {
        TableSession session = tableSessionRepository.findById(sessionId).orElseThrow();
        try {
            var field = TableSession.class.getDeclaredField("expiresAt");
            field.setAccessible(true);
            field.set(session, expiresAt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tableSessionRepository.saveAndFlush(session);
    }
}
