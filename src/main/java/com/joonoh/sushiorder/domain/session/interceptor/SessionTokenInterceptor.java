package com.joonoh.sushiorder.domain.session.interceptor;

import com.joonoh.sushiorder.domain.session.entity.TableSession;
import com.joonoh.sushiorder.domain.session.exception.InvalidSessionTokenException;
import com.joonoh.sushiorder.domain.session.repository.TableSessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 손님 QR 세션 인증. 직원 인증(JWT)과 별개 체계 — /api/v1/order/** 등 손님 API에만 적용.
 * 상태를 바꾸지 않고 TableSession.isUsable()로 판단만 한다 (TableSession.java 참고).
 */
@Component
@RequiredArgsConstructor
public class SessionTokenInterceptor implements HandlerInterceptor {

    public static final String SESSION_TOKEN_HEADER = "X-Session-Token";
    public static final String TABLE_ID_ATTRIBUTE = "tableId";
    public static final String SESSION_ID_ATTRIBUTE = "sessionId";

    private final TableSessionRepository tableSessionRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(SESSION_TOKEN_HEADER);
        if (token == null || token.isBlank()) {
            throw new InvalidSessionTokenException("세션 토큰이 없습니다.");
        }

        TableSession session = tableSessionRepository.findBySessionToken(token)
                .orElseThrow(() -> new InvalidSessionTokenException("유효하지 않은 세션 토큰입니다."));

        if (!session.isUsable()) {
            throw new InvalidSessionTokenException("세션이 만료되었거나 종료되었습니다.");
        }

        request.setAttribute(TABLE_ID_ATTRIBUTE, session.getTableId());
        request.setAttribute(SESSION_ID_ATTRIBUTE, session.getId());
        return true;
    }
}
