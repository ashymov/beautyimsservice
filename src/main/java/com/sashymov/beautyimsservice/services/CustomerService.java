package com.sashymov.beautyimsservice.services;

import com.sashymov.beautyimsservice.models.entities.Customer;
import com.sashymov.beautyimsservice.respones.Response;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {
    Customer findByName(String name);
    Customer findByEmail(String email);
    Customer findByPhone(String phone);
    Response save(Customer customer);
    List<Customer> findAll();
    Response upload(MultipartFile multipartFile, Long customerId);
    Customer findById(Long customerId);


}
