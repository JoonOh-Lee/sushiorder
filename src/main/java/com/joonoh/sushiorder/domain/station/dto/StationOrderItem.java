package com.joonoh.sushiorder.domain.station.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StationOrderItem {
    @NotNull
    private Long stationId;
    @NotNull
    private Integer sortOrder;
}
