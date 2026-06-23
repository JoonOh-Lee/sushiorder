package com.joonoh.sushiorder.domain.staffcall.exception;

public class StaffCallNotFoundException extends RuntimeException {

    public StaffCallNotFoundException(Long id) {
        super("호출 내역을 찾을 수 없습니다. id=" + id);
    }
}
