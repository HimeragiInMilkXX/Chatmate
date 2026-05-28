package com.easyprofile.authlib.exception;

import org.springframework.http.HttpStatus;

public class ResourceConflictException extends AuthLibException {

    public ResourceConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
