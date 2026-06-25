package com.joonoh.sushiorder.domain.staffcall.dto;

import com.joonoh.sushiorder.domain.staffcall.entity.CallStatus;
import com.joonoh.sushiorder.domain.staffcall.entity.CallType;
import com.joonoh.sushiorder.domain.staffcall.entity.StaffCall;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StaffCallResponse {
    private Long id;
    private Long tableId;
    private Long sessionId;
    private CallType type;
    private String itemName;
    private CallStatus status;
    private LocalDateTime createdAt;

    public static StaffCallResponse from(StaffCall call) {
        return StaffCallResponse.builder()
                .id(call.getId())
                .tableId(call.getTableId())
                .sessionId(call.getSessionId())
                .type(call.getType())
                .itemName(call.getItemName())
                .status(call.getStatus())
                .createdAt(call.getCreatedAt())
                .build();
    }
}
