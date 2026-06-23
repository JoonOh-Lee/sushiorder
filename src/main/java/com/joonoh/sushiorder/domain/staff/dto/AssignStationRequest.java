package com.joonoh.sushiorder.domain.staff.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AssignStationRequest {

    @NotNull
    private Long stationId;
}
