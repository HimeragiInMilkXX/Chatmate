package com.easyprofile.authlib.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends AuthLibException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
