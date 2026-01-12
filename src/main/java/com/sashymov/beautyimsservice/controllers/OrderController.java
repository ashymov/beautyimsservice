package com.sashymov.beautyimsservice.controllers;

import com.sashymov.beautyimsservice.models.dto.CreateOrderDto;
import com.sashymov.beautyimsservice.models.entities.Order;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Response create(@RequestBody CreateOrderDto createOrderDto) {
        return orderService.save(createOrderDto);
    }

    @GetMapping("/getByCustomerId")
    public List<Order> getByCustomerId(Long customerId) {
        return orderService.findByCustomerId(customerId);
    }
    @GetMapping("/getByCustomerName")
    public List<Order> getByCustomerName(String customerName) {
        return orderService.findByCustomerName(customerName);
    }

    @GetMapping("/getByCustomerEmail")
    public List<Order> getByCustomerEmail(String customerEmail) {
        return orderService.findByCustomerEmail(customerEmail);
    }

    @GetMapping("/getByCustomerPhone")
    public List<Order> getByCustomerPhone(String customerPhone) {
        return orderService.findByCustomerPhone(customerPhone);
    }
}
