package com.sashymov.beautyimsservice.services;

import com.sashymov.beautyimsservice.models.dto.CompanyDto;
import com.sashymov.beautyimsservice.models.entities.Company;
import com.sashymov.beautyimsservice.respones.Response;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CompanyService {
    Response save(CompanyDto companyDto);
    Response update(CompanyDto companyDto);
    Response delete(Long id);
    Company findById(Long id);
    Response findByName(String name);
    Response findByEmail(String email);
    Response findAll();
    Response upload(MultipartFile file, Long customerId);



}
