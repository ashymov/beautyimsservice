package com.sashymov.beautyimsservice.controllers;

import com.sashymov.beautyimsservice.models.dto.TimeSlotDto;
import com.sashymov.beautyimsservice.models.dto.WorkingHoursDto;
import com.sashymov.beautyimsservice.models.dto.WorkingHoursUpsertDto;
import com.sashymov.beautyimsservice.services.WorkingHoursService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/working-hours")
public class WorkingHoursController {

    private final WorkingHoursService workingHoursService;

    public WorkingHoursController(WorkingHoursService workingHoursService) {
        this.workingHoursService = workingHoursService;
    }

    // создать/обновить часы на конкретный день недели
    @PostMapping("/upsert")
    public WorkingHoursDto upsert(@RequestBody WorkingHoursUpsertDto dto) {
        return workingHoursService.upsert(dto);
    }

    // получить все рабочие часы мастера
    @GetMapping("/user/{userId}")
    public List<WorkingHoursDto> getAll(@PathVariable Long userId) {
        return workingHoursService.getAllForUser(userId);
    }

    // получить свободные слоты (по 1 часу) на дату
    @GetMapping("/free-slots")
    public List<TimeSlotDto> freeSlots(
            @RequestParam Long userId,
            @RequestParam String date // yyyy-MM-dd
    ) {
        return workingHoursService.getFreeSlots(userId, LocalDate.parse(date));
    }
}
