package com.joonoh.sushiorder.global.config;

import com.joonoh.sushiorder.domain.session.interceptor.SessionTokenInterceptor;
import com.joonoh.sushiorder.global.ratelimit.RateLimitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final SessionTokenInterceptor sessionTokenInterceptor;
    private final RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1순위: 세션 토큰 검증 — SESSION_ID_ATTRIBUTE를 request에 심어줌
        registry.addInterceptor(sessionTokenInterceptor)
                .addPathPatterns("/api/v1/order/**", "/api/v1/call/**");

        // 2순위: Rate Limiting — /api/v1/order는 위에서 심은 SESSION_ID_ATTRIBUTE를 읽으므로 반드시 이후에 실행
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/api/v1/order", "/api/v1/session", "/api/v1/auth/login");
    }
}
