package com.sashymov.beautyimsservice.models.dto;

import com.sashymov.beautyimsservice.enums.CompanyStatus;
import lombok.Data;

import java.util.Date;

@Data
public class CompanyDto {
    private Long companyId;
    private String companyName;
    private String companyAddress;
    private String companyEmail;
    private String companyPhone;
}
