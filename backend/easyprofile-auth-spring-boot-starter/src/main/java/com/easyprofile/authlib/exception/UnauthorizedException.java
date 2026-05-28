package com.easyprofile.authlib.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AuthLibException {

    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
