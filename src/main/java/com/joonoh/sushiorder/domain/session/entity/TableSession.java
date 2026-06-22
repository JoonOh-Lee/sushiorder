package com.joonoh.sushiorder.domain.session.entity;

import com.joonoh.sushiorder.domain.session.exception.InvalidSessionStateException;
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
@Table(
        name = "table_session",
        uniqueConstraints = @UniqueConstraint(name = "uk_table_session_token", columnNames = "session_token")
)
@NoArgsConstructor
public class TableSession extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Aggregate 간 참조는 ID로만 — RestaurantTable과는 별개 Aggregate
    @Column(name = "table_id", nullable = false, updatable = false)
    private Long tableId;

    @Column(name = "session_token", nullable = false, length = 36, updatable = false)
    private String sessionToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SessionStatus status;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Version
    private Long version;

    @Builder(access = AccessLevel.PRIVATE)
    private TableSession(Long tableId, String sessionToken, SessionStatus status, LocalDateTime expiresAt) {
        this.tableId = tableId;
        this.sessionToken = sessionToken;
        this.status = status;
        this.expiresAt = expiresAt;
    }

    /**
     * 새 QR 세션 오픈.
     * - sessionToken: UUID 발급 (JWT 아님 — 직원이 임의로 무효화/상태변경 해야 해서 DB 검증 방식 채택)
     * - ttl: "얼마나 유효한지"는 호출 측(Service) 정책, 만료시각 계산은 도메인 책임
     */
    public static TableSession open(Long tableId, Duration ttl) {
        if (tableId == null) {
            throw new IllegalArgumentException("tableId는 필수입니다.");
        }
        if (ttl == null || ttl.isNegative() || ttl.isZero()) {
            throw new IllegalArgumentException("ttl은 양수여야 합니다.");
        }
        return TableSession.builder()
                .tableId(tableId)
                .sessionToken(UUID.randomUUID().toString())
                .status(SessionStatus.ACTIVE)
                .expiresAt(LocalDateTime.now().plus(ttl))
                .build();
    }

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
        if (status != SessionStatus.ACTIVE) {
            throw new InvalidSessionStateException(
                    "ACTIVE 상태의 세션만 종료 처리할 수 있습니다. 현재 상태: " + status);
        }
    }
}
