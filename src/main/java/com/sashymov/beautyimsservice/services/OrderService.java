package com.sashymov.beautyimsservice.services;

import com.sashymov.beautyimsservice.models.dto.CancelOrderDto;
import com.sashymov.beautyimsservice.models.dto.CreateOrderDto;
import com.sashymov.beautyimsservice.models.dto.RescheduleOrderDto;
import com.sashymov.beautyimsservice.models.entities.Order;
import com.sashymov.beautyimsservice.respones.Response;

import java.util.List;

public interface OrderService {
    Response save(CreateOrderDto createOrderDto);
    List<Order> findAll();
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByCustomerName(String customerName);
    List<Order> findByCustomerEmail(String customerEmail);
    List<Order> findByCustomerPhone(String customerPhone);
    Response cancel(Long orderId, CancelOrderDto dto);
    Response reschedule(Long orderId, RescheduleOrderDto dto);

}
