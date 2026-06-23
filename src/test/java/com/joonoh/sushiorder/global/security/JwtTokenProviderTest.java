package com.joonoh.sushiorder.global.security;

import com.joonoh.sushiorder.domain.staff.entity.StaffRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired private JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.secret}")
    private String secret;

    @Test
    @DisplayName("발급한 토큰을 파싱하면 username/role 클레임이 그대로 나온다")
    void createToken_and_parseClaims_roundTrip() {
        String token = jwtTokenProvider.createToken("staff1", StaffRole.ADMIN);

        Claims claims = jwtTokenProvider.parseClaims(token);

        assertThat(claims.getSubject()).isEqualTo("staff1");
        assertThat(claims.get("role", String.class)).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("형식이 잘못된 토큰은 거부한다")
    void parseClaims_malformedToken_throws() {
        assertThatThrownBy(() -> jwtTokenProvider.parseClaims("not-a-jwt"))
                .isInstanceOf(JwtException.class);
    }

    @Test
    @DisplayName("만료된 토큰은 거부한다")
    void parseClaims_expiredToken_throws() {
        JwtTokenProvider expiredTokenIssuer = new JwtTokenProvider(secret, -1000L);
        String expiredToken = expiredTokenIssuer.createToken("staff1", StaffRole.STAFF);

        assertThatThrownBy(() -> jwtTokenProvider.parseClaims(expiredToken))
                .isInstanceOf(JwtException.class);
    }
}
