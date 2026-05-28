package com.easyprofile.authlib.exception;

import org.springframework.http.HttpStatus;

public class AuthLibException extends RuntimeException {

    private final HttpStatus httpStatus;

    public AuthLibException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
