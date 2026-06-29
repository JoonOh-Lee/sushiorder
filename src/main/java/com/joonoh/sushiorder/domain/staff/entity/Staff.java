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

    // Aggregate 간 참조는 ID로만 — 출근할 때마다 바뀔 수 있어 직원 본인이 로그인 후 직접 설정
    @Column(name = "station_id")
    private Long stationId;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean onDuty = false;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean active = true;

    @Builder
    private Staff(String username, String encodedPassword, StaffRole role, Long stationId) {
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
        this.stationId = stationId;
    }

    /** 직원이 로그인 후 본인이 근무할 자리를 직접 지정 */
    public void assignStation(Long stationId) {
        this.stationId = stationId;
    }

    public void changeRole(StaffRole role) {
        this.role = role;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    /** 로그인 시 이전 세션의 onDuty 상태를 초기화 */
    public void resetDuty() {
        this.onDuty = false;
    }

    public void startDuty() {
        this.onDuty = true;
    }

    public void endDuty() {
        this.onDuty = false;
    }
}
