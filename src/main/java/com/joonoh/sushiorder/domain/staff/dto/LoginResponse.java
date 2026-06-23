package com.joonoh.sushiorder.domain.staff.dto;

import com.joonoh.sushiorder.domain.staff.entity.StaffRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String token;
    private String username;
    private StaffRole role;
    private Long stationId;
}
