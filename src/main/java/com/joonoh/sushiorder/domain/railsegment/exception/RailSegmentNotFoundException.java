package com.joonoh.sushiorder.domain.railsegment.exception;

public class RailSegmentNotFoundException extends RuntimeException {

    public RailSegmentNotFoundException(Long id) {
        super("레일 구간을 찾을 수 없습니다. id=" + id);
    }
}
