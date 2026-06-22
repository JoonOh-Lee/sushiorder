package com.joonoh.sushiorder.domain.session.repository;

import com.joonoh.sushiorder.domain.session.entity.SessionStatus;
import com.joonoh.sushiorder.domain.session.entity.TableSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TableSessionRepository extends JpaRepository<TableSession, Long> {

    /** QR 인증 인터셉터가 매 요청마다 토큰으로 세션 조회 — hot path */
    Optional<TableSession> findBySessionToken(String sessionToken);

    /** 테이블에 이미 활성 세션이 있는지 체크 — 중복 세션 생성 방지용 */
    Optional<TableSession> findByTableIdAndStatus(Long tableId, SessionStatus status);

    /** 만료 스케줄러가 일괄 처리 대상을 찾을 때 사용 */
    List<TableSession> findAllByStatusAndExpiresAtBefore(SessionStatus status, LocalDateTime time);
}
