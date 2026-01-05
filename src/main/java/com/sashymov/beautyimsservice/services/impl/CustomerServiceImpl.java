package com.sashymov.beautyimsservice.services.impl;

import com.sashymov.beautyimsservice.dao.CustomerRepo;
import com.sashymov.beautyimsservice.microservices.fileService.FileResponse;
import com.sashymov.beautyimsservice.microservices.fileService.FileServiceFeign;
import com.sashymov.beautyimsservice.models.entities.Customer;
import com.sashymov.beautyimsservice.models.entities.File;
import com.sashymov.beautyimsservice.models.entities.UserWork;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.CustomerService;
import com.sashymov.beautyimsservice.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepo customerRepo;
    private final FileServiceFeign fileServiceFeign;
    private final FileService fileService;

    public CustomerServiceImpl(CustomerRepo customerRepo, FileServiceFeign fileServiceFeign, FileService fileService) {
        this.customerRepo = customerRepo;
        this.fileServiceFeign = fileServiceFeign;
        this.fileService = fileService;
    }

    @Override
    public Customer findByName(String name) {
        return customerRepo.findByName(name);
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepo.findByEmail(email);
    }

    @Override
    public Customer findByPhone(String phone) {
        return customerRepo.findByPhone(phone);
    }

    @Override
    public Response save(Customer customer) {
        Response response = Response.getResponse();
        response.setObject(customerRepo.save(customer));
        return response;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepo.findAll();
    }

    @Override
    public Response upload(MultipartFile multipartFile, Long customerId) {
        Response response = Response.getResponse();
        Customer customer =  customerRepo.findById(customerId).orElse(null);
        if (customer == null) {
            response.setMessage("Customer Not Found");
            response.setStatus(0);
            return response;
        }
        FileResponse fileResponse = fileServiceFeign.upload(multipartFile);
        File file = new File();
        file.setFileDownloadUri(fileResponse.getDownloadUri());
        file.setFileName(fileResponse.getFileName());
        file.setFileType(fileResponse.getFileType());
        file.setSize(fileResponse.getSize());
        file = fileService.save(file);
        customer.setFile(file);
        customerRepo.save(customer);
        response.setObject(customer);



        return response;
    }

    @Override
    public Customer findById(Long customerId) {
        return customerRepo.findById(customerId).orElse(null);
    }
}
