package com.sashymov.beautyimsservice.controllers;

import com.sashymov.beautyimsservice.models.dto.CompanyDto;
import com.sashymov.beautyimsservice.models.dto.UserDto;
import com.sashymov.beautyimsservice.models.entities.Company;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.CompanyService;
import com.sashymov.beautyimsservice.services.FileService;
import com.sashymov.beautyimsservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping("/update")
    public Response updateCompany(@RequestBody CompanyDto companyDto) {
        return companyService.update(companyDto);
        }

        @DeleteMapping("/delete")
    public Response deleteCompany(@RequestParam Long id) {
        return companyService.delete(id);
        }

        @GetMapping("/getById")
    public Company getCompany(@RequestParam Long id) {
        return companyService.findById(id);
        }

        @GetMapping("/getByName")
    public Response getCompanyByName(@RequestParam String name) {
        return companyService.findByName(name);
        }

        @GetMapping("/getByEmail")
    public Response getCompanyByEmail(@RequestParam String email) {
        return companyService.findByEmail(email);
        }

        @GetMapping("/getAll")
    public Response getAllCompanies() {
        return companyService.findAll();
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public Response upload(@RequestPart MultipartFile file,@RequestParam Long companyId) {
        return companyService.upload(file,companyId);
    }
}

