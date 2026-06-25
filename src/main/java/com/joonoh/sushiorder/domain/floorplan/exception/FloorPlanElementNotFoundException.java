package com.joonoh.sushiorder.domain.floorplan.exception;

public class FloorPlanElementNotFoundException extends RuntimeException {

    public FloorPlanElementNotFoundException(Long id) {
        super("평면도 요소를 찾을 수 없습니다. id=" + id);
    }
}
