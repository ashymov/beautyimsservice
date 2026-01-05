package com.sashymov.beautyimsservice.controllers;

import com.sashymov.beautyimsservice.models.entities.Customer;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @PostMapping("/create")
    public Response create(@RequestBody Customer customer){
        return customerService.save(customer);
    }
}
