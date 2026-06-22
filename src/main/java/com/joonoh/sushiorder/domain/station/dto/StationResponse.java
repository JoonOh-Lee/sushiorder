package com.joonoh.sushiorder.domain.station.dto;

import com.joonoh.sushiorder.domain.station.entity.Station;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StationResponse {
    private Long id;
    private String name;
    private int sortOrder;
    private boolean active;

    public static StationResponse from(Station station){
        return StationResponse.builder()
                .id(station.getId())
                .name(station.getName())
                .sortOrder(station.getSortOrder())
                .active(station.isActive())
                .build();
    }
}
