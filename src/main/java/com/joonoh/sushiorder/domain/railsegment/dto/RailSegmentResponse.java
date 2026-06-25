package com.joonoh.sushiorder.domain.railsegment.dto;

import com.joonoh.sushiorder.domain.railsegment.entity.RailSegment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RailSegmentResponse {
    private Long id;
    private int sequenceOrder;
    private Long fromTableId;
    private Long toTableId;
    private boolean active;

    public static RailSegmentResponse from(RailSegment segment) {
        return RailSegmentResponse.builder()
                .id(segment.getId())
                .sequenceOrder(segment.getSequenceOrder())
                .fromTableId(segment.getFromTableId())
                .toTableId(segment.getToTableId())
                .active(segment.isActive())
                .build();
    }
}
