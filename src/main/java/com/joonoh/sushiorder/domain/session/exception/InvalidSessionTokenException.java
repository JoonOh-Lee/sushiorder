package com.joonoh.sushiorder.domain.session.exception;

public class InvalidSessionTokenException extends RuntimeException {

    public InvalidSessionTokenException(String message) {
        super(message);
    }
}
