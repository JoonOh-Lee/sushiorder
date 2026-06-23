package com.joonoh.sushiorder.domain.staff.dto;

import com.joonoh.sushiorder.domain.staff.entity.Staff;
import com.joonoh.sushiorder.domain.staff.entity.StaffRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StaffMeResponse {
    private Long id;
    private String username;
    private StaffRole role;
    private Long stationId;

    public static StaffMeResponse from(Staff staff) {
        return StaffMeResponse.builder()
                .id(staff.getId())
                .username(staff.getUsername())
                .role(staff.getRole())
                .stationId(staff.getStationId())
                .build();
    }
}
