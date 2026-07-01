package com.joonoh.sushiorder.domain.session.entity;

import com.joonoh.sushiorder.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "table_session", indexes = {
        // 중복 세션 생성 방지 체크(findByTableIdAndStatus) — QR 스캔마다 호출되는 hot path
        @Index(name = "idx_table_session_table_id_status", columnList = "table_id, status"),
        // 만료 스케줄러의 findAllByStatusAndExpiresAtBefore
        @Index(name = "idx_table_session_status_expires_at", columnList = "status, expires_at")
})
public class TableSession extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Aggregate 간 참조는 ID로만 — RestaurantTable과는 별개 Aggregate
    @Column(name = "table_id", nullable = false)
    private Long tableId;

    @Column(name = "session_token", nullable = false, unique = true, length = 36)
    private String sessionToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SessionStatus status;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Version
    private Long version;

    @Builder
    private TableSession(Long tableId, Duration ttl) {
        if (tableId == null) {
            throw new IllegalArgumentException("tableId는 필수입니다.");
        }
        if (ttl == null || ttl.isNegative() || ttl.isZero()) {
            throw new IllegalArgumentException("ttl은 양수여야 합니다.");
        }
        this.tableId = tableId;
        this.sessionToken = UUID.randomUUID().toString();
        this.status = SessionStatus.ACTIVE;
        this.expiresAt = LocalDateTime.now().plus(ttl);
    }

    // ===== 비즈니스 로직 =====

    /** 정상 종료 — 직원이 테이블 정리/퇴실 처리 시 호출 */
    public void close() {
        validateActive();
        this.status = SessionStatus.CLOSED;
        this.endedAt = LocalDateTime.now();
    }

    /** 비정상 종료 — 만료 스케줄러가 호출 (정상 종료와 분석 단계에서 구분하기 위해 CLOSED와 분리) */
    public void expire() {
        validateActive();
        this.status = SessionStatus.EXPIRED;
        this.endedAt = LocalDateTime.now();
    }

    /** 인증 인터셉터가 호출. 상태를 바꾸지 않고 판단만 한다 (읽기 경로에 쓰기를 섞지 않음). */
    public boolean isUsable() {
        return status == SessionStatus.ACTIVE && expiresAt.isAfter(LocalDateTime.now());
    }

    private void validateActive() {
        if (this.status != SessionStatus.ACTIVE) {
            throw new IllegalStateException(
                    String.format("활성 상태의 세션만 종료 처리할 수 있습니다. 현재 상태: %s", this.status.getDisplayName()));
        }
    }
}
