package com.sashymov.beautyimsservice.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record WorkingHoursDto(
        Long userId,
        DayOfWeek dayOfWeek,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime startTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime endTime,
        boolean dayOff
) {}