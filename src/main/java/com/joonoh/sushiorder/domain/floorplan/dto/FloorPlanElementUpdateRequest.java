package com.joonoh.sushiorder.domain.floorplan.dto;

import com.joonoh.sushiorder.domain.floorplan.entity.FloorPlanElementType;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FloorPlanElementUpdateRequest {

    private FloorPlanElementType type;

    @Size(max = 50)
    private String label;
}
