package com.joonoh.sushiorder.domain.railsegment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RailSegmentReorderRequest {

    @NotEmpty(message = "orders는 비어 있을 수 없습니다.")
    @Valid
    private List<SegmentOrderItem> orders;

    @Getter
    @NoArgsConstructor
    public static class SegmentOrderItem {
        @NotNull
        private Long segmentId;
        @NotNull
        private Integer sequenceOrder;
    }
}
