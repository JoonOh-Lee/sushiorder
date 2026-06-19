package com.joonoh.sushiorder.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/v1/station/**").authenticated()  // 직원 로그인은 필요, ADMIN까진 아님
                    .requestMatchers("/api/v1/menu/**").permitAll()  // 메서드 무관, 경로로만 판단
                    .anyRequest().authenticated()
            )
            // JWT 필터 등 추가.
            .build();
        return http.build();
    }
}
