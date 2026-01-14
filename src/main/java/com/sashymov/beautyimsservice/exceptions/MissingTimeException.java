package com.sashymov.beautyimsservice.exceptions;

public class MissingTimeException extends BusinessException {
    public MissingTimeException() {
        super("MISSING_TIME", "StartTime/EndTime is required");
    }
}