package com.sashymov.beautyimsservice.services;

import java.time.LocalDateTime;

public interface WorkingHoursService {
    void validateWorkingHours( Long userId, LocalDateTime start, LocalDateTime end);
}
