package com.sashymov.beautyimsservice.services.impl;

import com.sashymov.beautyimsservice.dao.OrderRepo;
import com.sashymov.beautyimsservice.dao.UserWorkingHoursRepo;
import com.sashymov.beautyimsservice.exceptions.InvalidWorkingHoursException;
import com.sashymov.beautyimsservice.exceptions.NotFoundException;
import com.sashymov.beautyimsservice.models.dto.TimeSlotDto;
import com.sashymov.beautyimsservice.models.dto.WorkingHoursDto;
import com.sashymov.beautyimsservice.models.dto.WorkingHoursUpsertDto;
import com.sashymov.beautyimsservice.models.entities.Order;
import com.sashymov.beautyimsservice.models.entities.User;
import com.sashymov.beautyimsservice.models.entities.UserWorkingHours;
import com.sashymov.beautyimsservice.services.UserService;
import com.sashymov.beautyimsservice.services.WorkingHoursService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkingHoursServiceImpl implements WorkingHoursService {

    private static final Duration SLOT_DURATION = Duration.ofHours(1);

    private final UserWorkingHoursRepo repo;
    private final OrderRepo orderRepo;
    private final UserService userService;

    public WorkingHoursServiceImpl(UserWorkingHoursRepo repo, OrderRepo orderRepo, UserService userService) {
        this.repo = repo;
        this.orderRepo = orderRepo;
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public void validateWorkingHours(Long userId, LocalDateTime start, LocalDateTime end) {
        if (userId == null || start == null || end == null) {
            throw new InvalidWorkingHoursException("userId/start/end are required");
        }
        if (!end.isAfter(start)) {
            throw new InvalidWorkingHoursException("end must be after start");
        }

        UserWorkingHours wh = repo.findByUserIdAndDayOfWeek(userId, start.getDayOfWeek())
                .orElseThrow(() -> new NotFoundException("UserWorkingHours", userId));

        if (wh.isDayOff()) {
            throw new InvalidWorkingHoursException("Day off");
        }

        LocalDateTime workStart = LocalDateTime.of(start.toLocalDate(), wh.getStartTime());
        LocalDateTime workEnd = LocalDateTime.of(start.toLocalDate(), wh.getEndTime());

        if (!workEnd.isAfter(workStart)) {
            throw new InvalidWorkingHoursException("Working hours are invalid");
        }

        if (start.isBefore(workStart) || end.isAfter(workEnd)) {
            throw new InvalidWorkingHoursException("Outside working hours");
        }
    }

    @Override
    @Transactional
    public WorkingHoursDto upsert(WorkingHoursUpsertDto dto) {
        if (dto == null) {
            throw new InvalidWorkingHoursException("Body is required");
        }
        if (dto.userId() == null || dto.dayOfWeek() == null) {
            throw new InvalidWorkingHoursException("userId and dayOfWeek are required");
        }

        boolean dayOff = dto.dayOff();

        if (!dayOff) {
            if (dto.startTime() == null || dto.endTime() == null) {
                throw new InvalidWorkingHoursException("startTime/endTime are required");
            }
            if (!dto.endTime().isAfter(dto.startTime())) {
                throw new InvalidWorkingHoursException("endTime must be after startTime");
            }
        }

        User user = userService.findById(dto.userId());

        UserWorkingHours wh = repo.findByUserIdAndDayOfWeek(dto.userId(), dto.dayOfWeek())
                .orElseGet(UserWorkingHours::new);

        wh.setUser(user);
        wh.setDayOfWeek(dto.dayOfWeek());
        wh.setDayOff(dayOff);

        if (dayOff) {
            wh.setStartTime(LocalTime.MIDNIGHT);
            wh.setEndTime(LocalTime.MIDNIGHT);
        } else {
            wh.setStartTime(dto.startTime());
            wh.setEndTime(dto.endTime());
        }



        UserWorkingHours saved = repo.save(wh);

        return new WorkingHoursDto(
                saved.getUser().getId(),
                saved.getDayOfWeek(),
                saved.getStartTime(),
                saved.getEndTime(),
                saved.isDayOff()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkingHoursDto> getAllForUser(Long userId) {
        if (userId == null) {
            throw new InvalidWorkingHoursException("userId is required");
        }

        return repo.findAllByUserId(userId).stream()
                .map(wh -> new WorkingHoursDto(
                        wh.getUser().getId(),
                        wh.getDayOfWeek(),
                        wh.getStartTime(),
                        wh.getEndTime(),
                        wh.isDayOff()
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeSlotDto> getFreeSlots(Long userId, LocalDate date) {
        if (userId == null || date == null) {
            throw new InvalidWorkingHoursException("userId and date are required");
        }

        UserWorkingHours wh = repo.findByUserIdAndDayOfWeek(userId, date.getDayOfWeek())
                .orElseThrow(() -> new NotFoundException("UserWorkingHours", userId));

        if (wh.isDayOff()) {
            return List.of();
        }

        LocalDateTime workStart = LocalDateTime.of(date, wh.getStartTime());
        LocalDateTime workEnd = LocalDateTime.of(date, wh.getEndTime());

        if (!workEnd.isAfter(workStart)) {
            throw new InvalidWorkingHoursException("Working hours are invalid for " + date.getDayOfWeek());
        }

        List<Order> busyOrders = orderRepo.findActiveOrdersInRange(userId, workStart, workEnd);

        List<TimeSlotDto> result = new ArrayList<>();
        LocalDateTime cursor = workStart;

        while (!cursor.plus(SLOT_DURATION).isAfter(workEnd)) {
            LocalDateTime slotStart = cursor;
            LocalDateTime slotEnd = cursor.plus(SLOT_DURATION);

            boolean overlaps = busyOrders.stream().anyMatch(o ->
                    o.getStartTime().isBefore(slotEnd) && o.getEndTime().isAfter(slotStart)
            );

            if (!overlaps) {
                result.add(new TimeSlotDto(slotStart, slotEnd));
            }

            cursor = cursor.plus(SLOT_DURATION);
        }

        return result;
    }
}
