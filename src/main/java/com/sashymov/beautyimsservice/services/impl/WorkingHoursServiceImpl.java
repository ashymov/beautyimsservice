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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkingHoursServiceImpl implements WorkingHoursService {
    private static final Logger log = LoggerFactory.getLogger(WorkingHoursServiceImpl.class);

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
            log.error("Missing required parameters: userId={}, start={}, end={}", userId, start, end);
            throw new InvalidWorkingHoursException("userId/start/end are required");
        }

        if (!end.isAfter(start)) {
            log.error("End time {} is not after start time {}", end, start);
            throw new InvalidWorkingHoursException("end must be after start");
        }

        DayOfWeek dayOfWeek = start.getDayOfWeek();
        log.info("🔍 Looking for working hours: userId={}, dayOfWeek={}, start={}", userId, dayOfWeek, start);

        UserWorkingHours wh = repo.findByUserIdAndDayOfWeek(userId, dayOfWeek)
                .orElseThrow(() -> {
                    log.error("❌ No working hours found for userId={} on {}", userId, dayOfWeek);
                    return new NotFoundException("UserWorkingHours", userId);
                });

        log.info("✅ Found working hours: id={}, startTime={}, endTime={}, dayOff={}",
                wh.getId(), wh.getStartTime(), wh.getEndTime(), wh.isDayOff());

        if (wh.isDayOff()) {
            log.error("❌ User is off on {}", dayOfWeek);
            throw new InvalidWorkingHoursException("Day off on " + dayOfWeek);
        }

        LocalDateTime workStart = LocalDateTime.of(start.toLocalDate(), wh.getStartTime());
        LocalDateTime workEnd = LocalDateTime.of(start.toLocalDate(), wh.getEndTime());

        log.info("Work hours for this date: {} - {}", workStart, workEnd);

        if (!workEnd.isAfter(workStart)) {
            log.error("Invalid working hours configuration: start={}, end={}", workStart, workEnd);
            throw new InvalidWorkingHoursException("Working hours are invalid");
        }

        if (start.isBefore(workStart) || end.isAfter(workEnd)) {
            log.error("Requested time {} - {} is outside working hours {} - {}",
                    start, end, workStart, workEnd);
            throw new InvalidWorkingHoursException("Outside working hours");
        }

        log.info("✅ Working hours validation passed for userId={}", userId);
    }

    @Override
    @Transactional
    public WorkingHoursDto upsert(WorkingHoursUpsertDto dto) {
        if (dto == null) {
            log.error("Upsert called with null DTO");
            throw new InvalidWorkingHoursException("Body is required");
        }

        log.info("Upserting working hours: userId={}, dayOfWeek={}, dayOff={}",
                dto.userId(), dto.dayOfWeek(), dto.dayOff());

        if (dto.userId() == null || dto.dayOfWeek() == null) {
            log.error("Missing required fields: userId={}, dayOfWeek={}", dto.userId(), dto.dayOfWeek());
            throw new InvalidWorkingHoursException("userId and dayOfWeek are required");
        }

        boolean dayOff = dto.dayOff();

        if (!dayOff) {
            if (dto.startTime() == null || dto.endTime() == null) {
                log.error("Working day missing start/end time");
                throw new InvalidWorkingHoursException("startTime/endTime are required");
            }
            if (!dto.endTime().isAfter(dto.startTime())) {
                log.error("End time {} is not after start time {}", dto.endTime(), dto.startTime());
                throw new InvalidWorkingHoursException("endTime must be after startTime");
            }
        }

        User user = userService.findById(dto.userId());

        UserWorkingHours wh = repo.findByUserIdAndDayOfWeek(dto.userId(), dto.dayOfWeek())
                .orElseGet(() -> {
                    log.info("Creating new working hours entry for userId={}, dayOfWeek={}",
                            dto.userId(), dto.dayOfWeek());
                    return new UserWorkingHours();
                });

        wh.setUser(user);
        wh.setDayOfWeek(dto.dayOfWeek());
        wh.setDayOff(dayOff);

        if (dayOff) {
            wh.setStartTime(LocalTime.MIDNIGHT);
            wh.setEndTime(LocalTime.MIDNIGHT);
            log.debug("Set day off for userId={}, dayOfWeek={}", dto.userId(), dto.dayOfWeek());
        } else {
            wh.setStartTime(dto.startTime());
            wh.setEndTime(dto.endTime());
            log.debug("Set working hours for userId={}, dayOfWeek={}: {} - {}",
                    dto.userId(), dto.dayOfWeek(), dto.startTime(), dto.endTime());
        }

        UserWorkingHours saved = repo.save(wh);
        log.info("Successfully saved working hours with id={} for userId={}, dayOfWeek={}",
                saved.getId(), saved.getUser().getId(), saved.getDayOfWeek());

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
            log.error("getAllForUser called with null userId");
            throw new InvalidWorkingHoursException("userId is required");
        }

        log.info("Fetching all working hours for userId={}", userId);

        List<WorkingHoursDto> result = repo.findAllByUserId(userId).stream()
                .map(wh -> {
                    log.debug("Found working hours for userId={}, dayOfWeek={}, dayOff={}",
                            userId, wh.getDayOfWeek(), wh.isDayOff());
                    return new WorkingHoursDto(
                            wh.getUser().getId(),
                            wh.getDayOfWeek(),
                            wh.getStartTime(),
                            wh.getEndTime(),
                            wh.isDayOff()
                    );
                })
                .toList();

        log.info("Found {} working hour records for userId={}", result.size(), userId);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeSlotDto> getFreeSlots(Long userId, LocalDate date) {
        if (userId == null || date == null) {
            log.error("getFreeSlots called with null parameters: userId={}, date={}", userId, date);
            throw new InvalidWorkingHoursException("userId and date are required");
        }

        log.info("Getting free slots for userId={}, date={}", userId, date);

        UserWorkingHours wh = repo.findByUserIdAndDayOfWeek(userId, date.getDayOfWeek())
                .orElseThrow(() -> {
                    log.error("No working hours found for userId={} on day {}", userId, date.getDayOfWeek());
                    return new NotFoundException("UserWorkingHours", userId);
                });

        if (wh.isDayOff()) {
            log.info("User is off on {} - no free slots", date.getDayOfWeek());
            return List.of();
        }

        LocalDateTime workStart = LocalDateTime.of(date, wh.getStartTime());
        LocalDateTime workEnd = LocalDateTime.of(date, wh.getEndTime());

        log.debug("Work hours: {} - {}", workStart, workEnd);

        if (!workEnd.isAfter(workStart)) {
            log.error("Invalid working hours configuration for userId={} on {}", userId, date.getDayOfWeek());
            throw new InvalidWorkingHoursException("Working hours are invalid for " + date.getDayOfWeek());
        }

        List<Order> busyOrders = orderRepo.findActiveOrdersInRange(userId, workStart, workEnd);
        log.debug("Found {} active orders in range", busyOrders.size());

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

        log.info("Found {} free slots for userId={} on {}", result.size(), userId, date);
        return result;
    }
}