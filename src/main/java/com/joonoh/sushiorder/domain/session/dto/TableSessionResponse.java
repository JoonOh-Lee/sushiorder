package com.joonoh.sushiorder.domain.session.dto;

import com.joonoh.sushiorder.domain.restauranttable.entity.RestaurantTable;
import com.joonoh.sushiorder.domain.restauranttable.entity.SeatType;
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
    private SeatType seatType;
    private int tableNumber;
    private String sessionToken;
    private SessionStatus status;
    private LocalDateTime expiresAt;
    private LocalDateTime endedAt;

    /** 손님 QR 화면에서 "테이블1"/"다찌석1" 같은 좌석 라벨을 만들 때 seatType+tableNumber가 필요해서 RestaurantTable을 함께 받는다. */
    public static TableSessionResponse from(TableSession session, RestaurantTable table) {
        return TableSessionResponse.builder()
                .id(session.getId())
                .tableId(session.getTableId())
                .seatType(table.getSeatType())
                .tableNumber(table.getTableNumber())
                .sessionToken(session.getSessionToken())
                .status(session.getStatus())
                .expiresAt(session.getExpiresAt())
                .endedAt(session.getEndedAt())
                .build();
    }
}
