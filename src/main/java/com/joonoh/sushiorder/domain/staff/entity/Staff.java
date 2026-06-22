package com.joonoh.sushiorder.domain.staff.entity;

import com.joonoh.sushiorder.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "staff")
public class Staff extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    // 평문은 절대 들어오지 않음 — StaffService가 PasswordEncoder로 해시한 값만 전달
    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StaffRole role;

    @Builder
    private Staff(String username, String encodedPassword, StaffRole role) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username은 필수입니다.");
        }
        if (encodedPassword == null || encodedPassword.isBlank()) {
            throw new IllegalArgumentException("password는 필수입니다.");
        }
        if (role == null) {
            throw new IllegalArgumentException("role은 필수입니다.");
        }
        this.username = username;
        this.password = encodedPassword;
        this.role = role;
    }
}
