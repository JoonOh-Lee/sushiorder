package com.joonoh.sushiorder.global.config;

import com.joonoh.sushiorder.global.security.StompAuthChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Arrays;

/**
 * 직원 호출(StaffCall) 실시간 알림용 STOMP 설정. 손님은 구독하지 않고 직원 대시보드만 /topic/staff/calls를 구독한다.
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompAuthChannelInterceptor stompAuthChannelInterceptor;

    // SecurityConfig.corsAllowedOrigins와 동일한 프로퍼티/기본값 — REST와 WebSocket 모두 같은 프론트 출처를 허용해야 하므로 키를 공유한다.
    @Value("${app.cors.allowed-origins:http://localhost:3000,http://localhost:5173,http://172.31.99.96:5173}")
    private String corsAllowedOrigins;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/v1/ws")
                .setAllowedOrigins(Arrays.stream(corsAllowedOrigins.split(",")).map(String::trim).toArray(String[]::new));
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompAuthChannelInterceptor);
    }
}
