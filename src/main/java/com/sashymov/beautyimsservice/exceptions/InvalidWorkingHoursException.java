package com.sashymov.beautyimsservice.exceptions;

public class InvalidWorkingHoursException extends BusinessException {
    public InvalidWorkingHoursException(String message) {
        super("INVALID_WORKING_HOURS", message);
    }
}
