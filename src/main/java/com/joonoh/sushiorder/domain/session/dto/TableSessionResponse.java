package com.joonoh.sushiorder.domain.session.dto;

import com.joonoh.sushiorder.domain.session.entity.SessionStatus;
import com.joonoh.sushiorder.domain.session.entity.TableSession;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TableSessionResponse {
    private Long id;
    private Long tableId;
    private String sessionToken;
    private SessionStatus status;
    private LocalDateTime expiresAt;
    private LocalDateTime endedAt;

    public static TableSessionResponse from(TableSession session) {
        return TableSessionResponse.builder()
                .id(session.getId())
                .tableId(session.getTableId())
                .sessionToken(session.getSessionToken())
                .status(session.getStatus())
                .expiresAt(session.getExpiresAt())
                .endedAt(session.getEndedAt())
                .build();
    }
}
