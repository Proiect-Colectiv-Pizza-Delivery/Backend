package com.pizza.exception.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.core.AuthenticationException;

import java.io.Serial;
@ResponseStatus(HttpStatus.FORBIDDEN)
public class MissingDeviceIdException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = 1L;

    public MissingDeviceIdException(String message) {
        super(message);
    }

}