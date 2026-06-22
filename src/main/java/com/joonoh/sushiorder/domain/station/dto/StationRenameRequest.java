package com.joonoh.sushiorder.domain.station.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StationRenameRequest {
    @NotBlank
    private String name;
}
