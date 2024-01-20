package com.pizza.exception.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;
@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJwtForPathException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidJwtForPathException(String message) {
        super(message);
    }
}