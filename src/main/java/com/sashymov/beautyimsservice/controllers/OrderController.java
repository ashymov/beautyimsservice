package com.sashymov.beautyimsservice.controllers;

import com.sashymov.beautyimsservice.models.dto.CreateOrderDto;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Response create(@RequestBody CreateOrderDto createOrderDto) {
        return orderService.save(createOrderDto);
    }
}
