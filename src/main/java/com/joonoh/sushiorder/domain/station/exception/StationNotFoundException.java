package com.joonoh.sushiorder.domain.station.exception;

public class StationNotFoundException extends RuntimeException {
    public StationNotFoundException(Long id) {
        super("자리를 찾을 수 없습니다. id=" + id);
    }
}
