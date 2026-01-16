package com.sashymov.beautyimsservice.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record RescheduleOrderDto(

        @Schema(example = "2026-01-20 14:30")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime newStartTime
) {}