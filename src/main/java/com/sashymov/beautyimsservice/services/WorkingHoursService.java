package com.sashymov.beautyimsservice.services;

import com.sashymov.beautyimsservice.models.dto.TimeSlotDto;
import com.sashymov.beautyimsservice.models.dto.WorkingHoursDto;
import com.sashymov.beautyimsservice.models.dto.WorkingHoursUpsertDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface WorkingHoursService {

    // для OrderService: проверяем, что заказ внутри рабочих часов
    void validateWorkingHours(Long userId, LocalDateTime start, LocalDateTime end);

    // создать/обновить рабочие часы мастера на день недели
    WorkingHoursDto upsert(WorkingHoursUpsertDto dto);

    // получить все рабочие часы мастера (по дням недели)
    List<WorkingHoursDto> getAllForUser(Long userId);

    // получить свободные слоты на дату (по 1 часу)
    List<TimeSlotDto> getFreeSlots(Long userId, LocalDate date);
}
