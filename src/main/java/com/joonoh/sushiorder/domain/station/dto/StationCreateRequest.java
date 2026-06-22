package com.joonoh.sushiorder.domain.station.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StationCreateRequest {
    @NotBlank
    private String name;
}
