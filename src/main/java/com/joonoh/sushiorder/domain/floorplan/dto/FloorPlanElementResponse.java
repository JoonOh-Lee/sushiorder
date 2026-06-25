package com.joonoh.sushiorder.domain.floorplan.dto;

import com.joonoh.sushiorder.domain.floorplan.entity.FloorPlanElement;
import com.joonoh.sushiorder.domain.floorplan.entity.FloorPlanElementType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FloorPlanElementResponse {
    private Long id;
    private FloorPlanElementType type;
    private String label;
    private Double x;
    private Double y;
    private Double width;
    private Double height;

    public static FloorPlanElementResponse from(FloorPlanElement element) {
        return FloorPlanElementResponse.builder()
                .id(element.getId())
                .type(element.getType())
                .label(element.getLabel())
                .x(element.getX())
                .y(element.getY())
                .width(element.getWidth())
                .height(element.getHeight())
                .build();
    }
}
