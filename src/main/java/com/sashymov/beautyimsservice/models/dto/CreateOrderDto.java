package com.sashymov.beautyimsservice.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sashymov.beautyimsservice.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


public record    CreateOrderDto (

            Long userId,
            Long customerId,
            List<Long> userWorksId,
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
            @Schema(
                    type = "string")
            LocalDateTime startTime
    ) {}
