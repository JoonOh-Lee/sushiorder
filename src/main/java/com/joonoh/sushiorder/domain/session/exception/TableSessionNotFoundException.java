package com.joonoh.sushiorder.domain.session.exception;

public class TableSessionNotFoundException extends RuntimeException {

    public TableSessionNotFoundException(Long id) {
        super("세션을 찾을 수 없습니다. id=" + id);
    }

    public TableSessionNotFoundException(String sessionToken) {
        super("세션을 찾을 수 없습니다. token=" + sessionToken);
    }
}
