package com.joonoh.sushiorder.domain.staff.exception;

public class StaffNotFoundException extends RuntimeException {

    public StaffNotFoundException(String username) {
        super("직원을 찾을 수 없습니다. username=" + username);
    }

    public StaffNotFoundException(Long id) {
        super("직원을 찾을 수 없습니다. id=" + id);
    }
}
