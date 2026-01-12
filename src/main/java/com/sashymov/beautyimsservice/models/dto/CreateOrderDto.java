package com.sashymov.beautyimsservice.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sashymov.beautyimsservice.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateOrderDto {

        private Long userId;

        private List<Long> userWorksId;

        private Long customerId;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        @Schema(example = "2026-01-12 12:00", type = "string")
        private LocalDateTime startTime;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        @Schema(example = "2026-01-12 13:00", type = "string")
        private LocalDateTime endTime;
    }
