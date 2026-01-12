package com.sashymov.beautyimsservice.controllers;

import com.sashymov.beautyimsservice.models.entities.Customer;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/create")
    public Response create(@RequestBody Customer customer){
        return customerService.save(customer);
    }

    @GetMapping("/getByName")
    public Customer findByName(@RequestParam String name){
        return customerService.findByName(name);
    }

    @GetMapping("/getByEmail")
    public Customer findByEmail(@RequestParam String email){
        return customerService.findByEmail(email);
    }

    @GetMapping("/getByPhone")
    public Customer findByPhone(@RequestParam String phone){
        return customerService.findByPhone(phone);
    }

    @GetMapping("/getAll")
    public List<Customer> findAll(){
        return customerService.findAll();
    }

    @GetMapping("/getById")
    public Customer findById(Long customerId){
        return customerService.findById(customerId);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public Response upload(@RequestPart MultipartFile file,@RequestParam Long customerId)  {
        return customerService.upload(file,customerId);
    }
}
