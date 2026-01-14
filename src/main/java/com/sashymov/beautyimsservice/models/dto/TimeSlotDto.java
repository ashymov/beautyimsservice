package com.sashymov.beautyimsservice.models.dto;

import java.time.LocalDateTime;

public record TimeSlotDto(LocalDateTime start, LocalDateTime end) {}