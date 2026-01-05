package com.sashymov.beautyimsservice.models.dto;

import com.sashymov.beautyimsservice.models.entities.Company;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private Company company;
    private String phone;
    private String email;
}

