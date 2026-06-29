package com.joonoh.sushiorder.domain.staff.dto;

import com.joonoh.sushiorder.domain.staff.entity.Staff;
import com.joonoh.sushiorder.domain.staff.entity.StaffRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StaffResponse {
    private Long id;
    private String username;
    private StaffRole role;
    private Long stationId;
    private boolean onDuty;
    private boolean active;

    public static StaffResponse from(Staff staff) {
        return StaffResponse.builder()
                .id(staff.getId())
                .username(staff.getUsername())
                .role(staff.getRole())
                .stationId(staff.getStationId())
                .onDuty(staff.isOnDuty())
                .active(staff.isActive())
                .build();
    }
}
