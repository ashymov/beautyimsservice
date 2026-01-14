package com.sashymov.beautyimsservice.exceptions;

public class NotFoundException extends BusinessException {

    public NotFoundException(String entity, Long id) {
        super("NOT_FOUND", entity + " with id " + id + " not found");
    }
}
