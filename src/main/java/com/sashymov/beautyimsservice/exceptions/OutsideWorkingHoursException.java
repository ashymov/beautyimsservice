package com.sashymov.beautyimsservice.exceptions;

public class OutsideWorkingHoursException extends BusinessException {

    public OutsideWorkingHoursException(String message) {
        super("OUTSIDE_WORKING_HOURS", message);
    }
}
