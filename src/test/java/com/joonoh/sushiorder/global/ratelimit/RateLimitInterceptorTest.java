package com.joonoh.sushiorder.global.ratelimit;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

class RateLimitInterceptorTest {

    private final RateLimitInterceptor interceptor = new RateLimitInterceptor();

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("admin 엔드포인트가 아니면(GET) 제한하지 않는다")
    void nonMutatingRequest_isNeverLimited() throws Exception {
        loginAs("admin1");

        for (int i = 0; i < 100; i++) {
            assertThat(interceptor.preHandle(mutatingRequest("GET", "/api/v1/admin/menu"), new MockHttpServletResponse(), null))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("같은 직원이 admin 쓰기 요청을 분당 30회 초과하면 차단된다")
    void adminEndpoint_blocksAfterThirtyRequestsPerMinute() throws Exception {
        loginAs("admin1");

        for (int i = 0; i < 30; i++) {
            HttpServletRequest request = mutatingRequest("POST", "/api/v1/admin/menu");
            assertThat(interceptor.preHandle(request, new MockHttpServletResponse(), null)).isTrue();
        }

        MockHttpServletResponse blockedResponse = new MockHttpServletResponse();
        boolean allowed = interceptor.preHandle(mutatingRequest("POST", "/api/v1/admin/menu"), blockedResponse, null);

        assertThat(allowed).isFalse();
        assertThat(blockedResponse.getStatus()).isEqualTo(429);
    }

    @Test
    @DisplayName("같은 직원이 staff 쓰기 요청을 분당 60회 초과하면 차단된다")
    void staffEndpoint_blocksAfterSixtyRequestsPerMinute() throws Exception {
        loginAs("staff1");

        for (int i = 0; i < 60; i++) {
            assertThat(interceptor.preHandle(mutatingRequest("PATCH", "/api/v1/staff/order/1/confirm"), new MockHttpServletResponse(), null))
                    .isTrue();
        }

        MockHttpServletResponse blockedResponse = new MockHttpServletResponse();
        boolean allowed = interceptor.preHandle(mutatingRequest("PATCH", "/api/v1/staff/order/1/confirm"), blockedResponse, null);

        assertThat(allowed).isFalse();
    }

    @Test
    @DisplayName("직원마다 버킷이 분리되어 있어 한 명이 다 써도 다른 직원은 영향받지 않는다")
    void differentActors_haveIndependentBuckets() throws Exception {
        loginAs("admin1");
        for (int i = 0; i < 30; i++) {
            interceptor.preHandle(mutatingRequest("POST", "/api/v1/admin/menu"), new MockHttpServletResponse(), null);
        }
        assertThat(interceptor.preHandle(mutatingRequest("POST", "/api/v1/admin/menu"), new MockHttpServletResponse(), null))
                .isFalse();

        loginAs("admin2");
        assertThat(interceptor.preHandle(mutatingRequest("POST", "/api/v1/admin/menu"), new MockHttpServletResponse(), null))
                .isTrue();
    }

    private void loginAs(String username) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(username, null, java.util.List.of()));
    }

    private HttpServletRequest mutatingRequest(String method, String uri) {
        MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
        request.setRequestURI(uri);
        return request;
    }
}
