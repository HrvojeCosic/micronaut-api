package com.base.exceptions;

import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.http.HttpStatus;

public class ResourceNotFoundException extends HttpStatusException {

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}