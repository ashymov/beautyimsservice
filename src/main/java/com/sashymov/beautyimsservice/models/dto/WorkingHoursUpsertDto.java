package com.sashymov.beautyimsservice.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record WorkingHoursUpsertDto(
        Long userId,
        DayOfWeek dayOfWeek,
        @Schema(example = "09:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime startTime,
        @Schema(example = "18:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime endTime,
        boolean dayOff
) {}