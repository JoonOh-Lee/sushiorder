package com.joonoh.sushiorder.global.config;

import com.joonoh.sushiorder.domain.session.interceptor.SessionTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final SessionTokenInterceptor sessionTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionTokenInterceptor)
                .addPathPatterns("/api/v1/order/**");
    }
}
