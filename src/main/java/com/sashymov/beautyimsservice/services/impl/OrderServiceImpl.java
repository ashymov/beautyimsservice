package com.sashymov.beautyimsservice.services.impl;

import com.sashymov.beautyimsservice.dao.OrderRepo;
import com.sashymov.beautyimsservice.enums.OrderStatus;
import com.sashymov.beautyimsservice.models.dto.CreateOrderDto;
import com.sashymov.beautyimsservice.models.entities.Order;
import com.sashymov.beautyimsservice.models.entities.UserWork;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.CustomerService;
import com.sashymov.beautyimsservice.services.OrderService;
import com.sashymov.beautyimsservice.services.UserService;
import com.sashymov.beautyimsservice.services.UserWorkService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orderRepo;
    private final UserService userService;
    private final CustomerService customerService;
    private final UserWorkService  userWorkService;

    public OrderServiceImpl(OrderRepo orderRepo, UserService userService, CustomerService customerService, UserWorkService userWorkService) {
        this.orderRepo = orderRepo;
        this.userService = userService;
        this.customerService = customerService;
        this.userWorkService = userWorkService;
    }

    @Override
    public Response save(CreateOrderDto createOrderDto) {
        Response response = Response.getResponse();
        List<UserWork> userWorks = userWorkService.findAllByIdIn(createOrderDto.getUserWorksId());
        double price = userWorks.stream().mapToDouble(UserWork::getPrice).sum();
        Order order = new Order();
        order.setStatus(OrderStatus.ACTIVE);
        order.setUser(userService.findById(createOrderDto.getUserId()));
        order.setCustomer(customerService.findById(createOrderDto.getCustomerId()));
        order.setUserWorks(userWorkService.findAllByIdIn(createOrderDto.getUserWorksId()));
        order.setDate(new Date());
        order.setPrice(price);
        orderRepo.save(order);
        response.setObject(order);
        return response;
    }

    @Override
    public List<Order> findAll() {
        return orderRepo.findAll();
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        return orderRepo.findAllByCustomerId(customerId);
    }

    @Override
    public List<Order> findByCustomerName(String customerName) {
        return orderRepo.findAllByCustomerName(customerName);
    }

    @Override
    public List<Order> findByCustomerEmail(String customerEmail) {
        return orderRepo.findAllByCustomerEmail(customerEmail);
    }

    @Override
    public List<Order> findByCustomerPhone(String customerPhone) {
        return orderRepo.findAllByCustomerPhone(customerPhone);
    }
}
