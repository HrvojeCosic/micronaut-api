package com.base.exceptions;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;

public class InvalidResourceIdException extends HttpStatusException {

    public InvalidResourceIdException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}