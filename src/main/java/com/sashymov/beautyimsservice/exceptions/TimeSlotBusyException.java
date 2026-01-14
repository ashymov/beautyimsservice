package com.sashymov.beautyimsservice.exceptions;

public class TimeSlotBusyException extends BusinessException {

    public TimeSlotBusyException() {
        super("TIME_SLOT_BUSY", "Time slot is busy");
    }
}