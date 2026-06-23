package com.joonoh.sushiorder.domain.session.interceptor;

import com.joonoh.sushiorder.domain.restauranttable.entity.RestaurantTable;
import com.joonoh.sushiorder.domain.restauranttable.entity.SeatType;
import com.joonoh.sushiorder.domain.restauranttable.repository.RestaurantTableRepository;
import com.joonoh.sushiorder.domain.session.dto.TableSessionResponse;
import com.joonoh.sushiorder.domain.session.exception.InvalidSessionTokenException;
import com.joonoh.sushiorder.domain.session.repository.TableSessionRepository;
import com.joonoh.sushiorder.domain.session.service.TableSessionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class SessionTokenInterceptorTest {

    @Autowired private SessionTokenInterceptor sessionTokenInterceptor;
    @Autowired private TableSessionService tableSessionService;
    @Autowired private TableSessionRepository tableSessionRepository;
    @Autowired private RestaurantTableRepository restaurantTableRepository;

    private Long tableId;

    @BeforeEach
    void setUp() {
        RestaurantTable table = RestaurantTable.builder()
                .seatType(SeatType.TABLE)
                .tableNumber(9101)
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
    @DisplayName("토큰 헤더가 없으면 거부한다")
    void preHandle_missingHeader_throws() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThatThrownBy(() -> sessionTokenInterceptor.preHandle(request, response, new Object()))
                .isInstanceOf(InvalidSessionTokenException.class);
    }

    @Test
    @DisplayName("존재하지 않는 토큰이면 거부한다")
    void preHandle_unknownToken_throws() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(SessionTokenInterceptor.SESSION_TOKEN_HEADER, "no-such-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThatThrownBy(() -> sessionTokenInterceptor.preHandle(request, response, new Object()))
                .isInstanceOf(InvalidSessionTokenException.class);
    }

    @Test
    @DisplayName("유효한 토큰이면 통과시키고 tableId/sessionId를 request attribute로 세팅한다")
    void preHandle_validToken_setsAttributes() {
        TableSessionResponse session = tableSessionService.createSession(tableId);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(SessionTokenInterceptor.SESSION_TOKEN_HEADER, session.getSessionToken());
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean result = sessionTokenInterceptor.preHandle(request, response, new Object());

        assertThat(result).isTrue();
        assertThat(request.getAttribute(SessionTokenInterceptor.TABLE_ID_ATTRIBUTE)).isEqualTo(tableId);
        assertThat(request.getAttribute(SessionTokenInterceptor.SESSION_ID_ATTRIBUTE)).isEqualTo(session.getId());
    }

    @Test
    @DisplayName("종료된 세션의 토큰이면 거부한다")
    void preHandle_closedSession_throws() {
        TableSessionResponse session = tableSessionService.createSession(tableId);
        tableSessionService.closeSession(session.getId());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(SessionTokenInterceptor.SESSION_TOKEN_HEADER, session.getSessionToken());
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThatThrownBy(() -> sessionTokenInterceptor.preHandle(request, response, new Object()))
                .isInstanceOf(InvalidSessionTokenException.class);
    }
}
