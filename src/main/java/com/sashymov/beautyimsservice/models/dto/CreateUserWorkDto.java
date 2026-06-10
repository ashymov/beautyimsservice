package com.sashymov.beautyimsservice.models.dto;

import com.sashymov.beautyimsservice.models.entities.User;
import lombok.Data;

@Data
public class CreateUserWorkDto {
    private Long id;
    private String name;
    private double price;
    private Long userId;
    private int durationMinutes;
}
