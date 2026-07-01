package com.joonoh.sushiorder.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String JWT_SCHEME_NAME = "JWT";

    // Swagger UI의 "Authorize" 버튼에 넣은 토큰이 모든 요청에 Authorization: Bearer {token}으로 자동 첨부되도록 전역 등록
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("sushiorder API").version("v1"))
                .addSecurityItem(new SecurityRequirement().addList(JWT_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(JWT_SCHEME_NAME,
                        new SecurityScheme()
                                .name(JWT_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

}
