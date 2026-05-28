package com.easyprofile.authlib.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends AuthLibException {

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
