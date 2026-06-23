package com.joonoh.sushiorder.domain.staff.service;

import com.joonoh.sushiorder.domain.staff.dto.LoginRequest;
import com.joonoh.sushiorder.domain.staff.dto.LoginResponse;
import com.joonoh.sushiorder.domain.staff.entity.Staff;
import com.joonoh.sushiorder.domain.staff.entity.StaffRole;
import com.joonoh.sushiorder.domain.staff.exception.InvalidCredentialsException;
import com.joonoh.sushiorder.domain.staff.repository.StaffRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class StaffServiceTest {

    @Autowired private StaffService staffService;
    @Autowired private StaffRepository staffRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private Long staffId;

    @BeforeEach
    void setUp() {
        Staff staff = Staff.builder()
                .username("test-staff")
                .encodedPassword(passwordEncoder.encode("correct-password"))
                .role(StaffRole.STAFF)
                .build();
        staffId = staffRepository.saveAndFlush(staff).getId();
    }

    @AfterEach
    void tearDown() {
        staffRepository.deleteById(staffId);
    }

    @Test
    @DisplayName("아이디/비밀번호가 맞으면 토큰을 발급한다")
    void login_success() {
        LoginResponse response = staffService.login(loginRequest("test-staff", "correct-password"));

        assertThat(response.getToken()).isNotBlank();
        assertThat(response.getUsername()).isEqualTo("test-staff");
        assertThat(response.getRole()).isEqualTo(StaffRole.STAFF);
    }

    @Test
    @DisplayName("존재하지 않는 아이디면 거부한다")
    void login_unknownUsername_throws() {
        assertThatThrownBy(() -> staffService.login(loginRequest("no-such-user", "whatever")))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    @DisplayName("비밀번호가 틀리면 거부한다")
    void login_wrongPassword_throws() {
        assertThatThrownBy(() -> staffService.login(loginRequest("test-staff", "wrong-password")))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    private LoginRequest loginRequest(String username, String password) {
        LoginRequest request = new LoginRequest();
        setField(request, "username", username);
        setField(request, "password", password);
        return request;
    }

    private void setField(Object target, String name, Object value) {
        try {
            var field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
