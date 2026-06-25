package com.joonoh.sushiorder.domain.floorplan.dto;

import com.joonoh.sushiorder.domain.floorplan.entity.FloorPlanElementType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FloorPlanElementCreateRequest {

    @NotNull
    private FloorPlanElementType type;

    @Size(max = 50)
    private String label;

    @NotNull
    @DecimalMin("0")
    @DecimalMax("100")
    private Double x;

    @NotNull
    @DecimalMin("0")
    @DecimalMax("100")
    private Double y;

    @NotNull
    @DecimalMin("0")
    @DecimalMax("100")
    private Double width;

    @NotNull
    @DecimalMin("0")
    @DecimalMax("100")
    private Double height;
}
