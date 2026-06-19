package com.joonoh.sushiorder.domain.menu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StationAssignRequest {
    @NotNull
    private Long stationId;
}
