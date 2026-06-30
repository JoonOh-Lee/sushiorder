package com.joonoh.sushiorder.global.ratelimit;

import com.joonoh.sushiorder.domain.session.interceptor.SessionTokenInterceptor;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 엔드포인트별 요청 횟수 제한.
 * - POST /api/v1/order      : 세션당 1분 5회
 * - POST /api/v1/session    : IP당 1분 3회
 * - POST /api/v1/auth/login : IP당 1분 10회
 *
 * SessionTokenInterceptor 이후에 등록되어야 /api/v1/order의 SESSION_ID_ATTRIBUTE를 읽을 수 있다.
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!"POST".equals(request.getMethod())) return true;

        String uri = request.getRequestURI();
        String key;
        int capacity;

        if ("/api/v1/order".equals(uri)) {
            Object sessionId = request.getAttribute(SessionTokenInterceptor.SESSION_ID_ATTRIBUTE);
            if (sessionId == null) return true; // SessionTokenInterceptor가 거부 처리
            key = "order:" + sessionId;
            capacity = 5;
        } else if ("/api/v1/session".equals(uri)) {
            key = "session:" + extractIp(request);
            capacity = 3;
        } else if ("/api/v1/auth/login".equals(uri)) {
            key = "login:" + extractIp(request);
            capacity = 10;
        } else {
            return true;
        }

        if (!getOrCreateBucket(key, capacity).tryConsume(1)) {
            send429(response);
            return false;
        }
        return true;
    }

    private Bucket getOrCreateBucket(String key, int capacity) {
        return buckets.computeIfAbsent(key, k -> Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(capacity)
                        .refillGreedy(capacity, Duration.ofMinutes(1))
                        .build())
                .build());
    }

    private void send429(HttpServletResponse response) throws Exception {
        response.setStatus(429);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                "{\"success\":false,\"data\":null,\"message\":\"요청이 너무 많습니다. 잠시 후 다시 시도해주세요.\"}");
    }

    private String extractIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) return forwarded.split(",")[0].trim();
        return request.getRemoteAddr();
    }
}
