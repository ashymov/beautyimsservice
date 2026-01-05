package com.sashymov.beautyimsservice.controllers;

import com.sashymov.beautyimsservice.models.dto.CompanyDto;
import com.sashymov.beautyimsservice.models.dto.UserDto;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.CompanyService;
import com.sashymov.beautyimsservice.services.FileService;
import com.sashymov.beautyimsservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {

    private final CompanyService companyService;
    private final FileService fileService;

    public CompanyController(CompanyService companyService, FileService fileService) {
        this.companyService = companyService;
        this.fileService = fileService;
    }

    @PostMapping("/create")
    public Response createCompany(@RequestBody CompanyDto companyDto) {
        return companyService.save(companyDto);
    }
}

