package com.sashymov.beautyimsservice.exceptions;

public class InvalidTimeRangeException extends BusinessException {

    public InvalidTimeRangeException() {
        super("INVALID_TIME_RANGE", "End time must be after start time");
    }
}
