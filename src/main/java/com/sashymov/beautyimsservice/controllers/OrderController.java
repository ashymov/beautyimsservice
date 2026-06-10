package com.sashymov.beautyimsservice.controllers;

import com.sashymov.beautyimsservice.models.dto.CancelOrderDto;
import com.sashymov.beautyimsservice.models.dto.CreateOrderDto;
import com.sashymov.beautyimsservice.models.dto.RescheduleOrderDto;
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

    @GetMapping("/getAll")
    public List<Order> getAll() {
        return orderService.findAll();
    }

    @GetMapping("/getByCustomerId")
    public List<Order> getByCustomerId(@RequestParam Long customerId) {
        return orderService.findByCustomerId(customerId);
    }

    @GetMapping("/getByCustomerName")
    public List<Order> getByCustomerName(@RequestParam String customerName) {
        return orderService.findByCustomerName(customerName);
    }

    @GetMapping("/getByCustomerEmail")
    public List<Order> getByCustomerEmail(@RequestParam String customerEmail) {
        return orderService.findByCustomerEmail(customerEmail);
    }

    @GetMapping("/getByCustomerPhone")
    public List<Order> getByCustomerPhone(@RequestParam String customerPhone) {
        return orderService.findByCustomerPhone(customerPhone);
    }

    @PostMapping("/cancel/{orderId}")
    public Response cancel(@PathVariable("orderId") Long orderId, @RequestBody CancelOrderDto cancelOrderDto) {
        return orderService.cancel(orderId, cancelOrderDto);
    }

    @PostMapping("/{orderId}/reschedule")
    public Response reschedule(@PathVariable Long orderId, @RequestBody RescheduleOrderDto dto) {
        return orderService.reschedule(orderId, dto);
    }
}