package com.sashymov.beautyimsservice.services.impl;

import com.sashymov.beautyimsservice.dao.CompanyRepo;
import com.sashymov.beautyimsservice.enums.CompanyStatus;
import com.sashymov.beautyimsservice.microservices.fileService.FileResponse;
import com.sashymov.beautyimsservice.microservices.fileService.FileServiceFeign;
import com.sashymov.beautyimsservice.models.dto.CompanyDto;
import com.sashymov.beautyimsservice.models.entities.Company;
import com.sashymov.beautyimsservice.models.entities.Customer;
import com.sashymov.beautyimsservice.models.entities.File;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.CompanyService;
import com.sashymov.beautyimsservice.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepo companyRepo;
    private final FileServiceFeign fileServiceFeign;
    private final FileService fileService;

    public CompanyServiceImpl(CompanyRepo companyRepo, FileServiceFeign fileServiceFeign, FileService fileService) {
        this.companyRepo = companyRepo;
        this.fileServiceFeign = fileServiceFeign;
        this.fileService = fileService;
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
        List<Company> companies = companyRepo.findByCompanyStatus(CompanyStatus.ACTIVE);
        response.setObject(companies);
        return response;
    }

    @Override
    public Response upload(MultipartFile file, Long customerId) {
        Response response = Response.getResponse();
        Company company =  companyRepo.findById(customerId).orElse(null);
        if (company == null) {
            response.setMessage("Company Not Found");
            response.setStatus(0);
            return response;
        }
        FileResponse fileResponse = fileServiceFeign.upload(file);
        File file1 = new File();
        file1.setFileDownloadUri(fileResponse.getDownloadUri());
        file1.setFileName(fileResponse.getFileName());
        file1.setFileType(fileResponse.getFileType());
        file1.setSize(fileResponse.getSize());
        file1 = fileService.save(file1);
        company.setFile(file1);
        companyRepo.save(company);
        response.setObject(company);



        return response;
    }

}
