package com.joonoh.sushiorder.domain.station.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class StationReorderRequest {
    @NotEmpty
    @Valid
    private List<StationOrderItem> orders;
}
