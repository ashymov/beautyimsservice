package com.sashymov.beautyimsservice.services.impl;

import com.sashymov.beautyimsservice.dao.CompanyRepo;
import com.sashymov.beautyimsservice.enums.CompanyStatus;
import com.sashymov.beautyimsservice.models.dto.CompanyDto;
import com.sashymov.beautyimsservice.models.entities.Company;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.CompanyService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepo companyRepo;

    public CompanyServiceImpl(CompanyRepo companyRepo) {
        this.companyRepo = companyRepo;
    }

    @Override
    public Response save(CompanyDto companyDto) {
        Response response = Response.getResponse();

        Company company = new Company();
        company.setCompanyAddress(companyDto.getCompanyAddress());
        company.setCompanyName(companyDto.getCompanyName());
        company.setCompanyPhone(companyDto.getCompanyPhone());
        company.setCompanyEmail(companyDto.getCompanyEmail());
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        company.setCreatedDate(new Date());
        companyRepo.save(company);
        response.setObject(company);
        return response;
    }

    @Override
    public Response update(CompanyDto companyDto) {
        Response response = Response.getResponse();
        Company company = findById(companyDto.getCompanyId());
        company.setCompanyAddress(companyDto.getCompanyAddress());
        company.setCompanyName(companyDto.getCompanyName());
        company.setCompanyPhone(companyDto.getCompanyPhone());
        company.setCompanyEmail(companyDto.getCompanyEmail());
        company.setUpdatedDate(new Date());
        companyRepo.save(company);
        response.setObject(company);


        return response;
    }

    @Override
    public Response delete(Long id) {
        Response response = Response.getResponse();

        Company company = findById(id);
    company.setCompanyStatus(CompanyStatus.ARCHIVED);
    companyRepo.save(company);
        return response;
    }

    @Override
    public Company findById(Long id) {
        return companyRepo.findById(id).orElse(null);
    }

    @Override
    public Response findByName(String name) {
        Response response = Response.getResponse();
        Company company = companyRepo.findByCompanyName(name);
        response.setObject(company);
        return response;
    }

    @Override
    public Response findByEmail(String email) {
        Response response = Response.getResponse();
        Company company = companyRepo.findByCompanyEmail(email);
        response.setObject(company);

        return response;
    }

    @Override
    public Response findAll() {
        Response response = Response.getResponse();
        List<Company> companies = companyRepo.findAll();
        response.setObject(companies);
        return response;
    }
}
