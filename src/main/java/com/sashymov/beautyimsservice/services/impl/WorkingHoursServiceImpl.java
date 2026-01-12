package com.sashymov.beautyimsservice.services.impl;

import com.sashymov.beautyimsservice.dao.UserWorkingHoursRepo;
import com.sashymov.beautyimsservice.models.entities.UserWorkingHours;
import com.sashymov.beautyimsservice.services.WorkingHoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkingHoursServiceImpl implements WorkingHoursService {
    private final UserWorkingHoursRepo userWorkingHoursRepo;

    @Override
    public void validateWorkingHours(Long userId, LocalDateTime start, LocalDateTime end) {
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException(
                    "Start time must be before end time");
        }
        if (!start.toLocalDate().equals(end.toLocalDate())) {
            throw new RuntimeException(
                    "Order must be within a single day");
        }

        List<UserWorkingHours> windows =
                userWorkingHoursRepo.findAllByUserIdAndDayOfWeek(
                        userId, start.getDayOfWeek());

        if (windows.isEmpty()) {
            throw new RuntimeException(
                    "User has no working hours for this day");
        }


        LocalTime orderStart = start.toLocalTime();
        LocalTime orderEnd = end.toLocalTime();

        boolean fitsAnyWindow = windows.stream()
                .anyMatch(w ->
                        !orderStart.isBefore(w.getStartTime())
                                && !orderEnd.isAfter(w.getEndTime())
                );

        if (!fitsAnyWindow) {
            throw new RuntimeException(
                    "Order is outside of working hours");
        }
    }
}