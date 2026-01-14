package com.sashymov.beautyimsservice.services.impl;

import com.sashymov.beautyimsservice.dao.OrderRepo;
import com.sashymov.beautyimsservice.dao.UserScheduleRepo;
import com.sashymov.beautyimsservice.enums.OrderStatus;
import com.sashymov.beautyimsservice.exceptions.InvalidTimeRangeException;
import com.sashymov.beautyimsservice.exceptions.MissingTimeException;
import com.sashymov.beautyimsservice.exceptions.TimeSlotBusyException;
import com.sashymov.beautyimsservice.models.dto.CreateOrderDto;
import com.sashymov.beautyimsservice.models.entities.*;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orderRepo;
    private final UserService userService;
    private final CustomerService customerService;
    private final UserWorkService  userWorkService;
    private final UserScheduleRepo userScheduleRepo;
    private final WorkingHoursService workingHoursService;

    public OrderServiceImpl(OrderRepo orderRepo, UserService userService, CustomerService customerService, UserWorkService userWorkService, UserScheduleRepo userScheduleRepo, WorkingHoursService workingHoursService) {
        this.orderRepo = orderRepo;
        this.userService = userService;
        this.customerService = customerService;
        this.userWorkService = userWorkService;
        this.userScheduleRepo = userScheduleRepo;
        this.workingHoursService = workingHoursService;
    }

    @Override
    public Response save(CreateOrderDto dto) {
        Response response = Response.getResponse();

        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new MissingTimeException();
        }
        if (!dto.getEndTime().isAfter(dto.getStartTime())) {
            throw new InvalidTimeRangeException();
        }

        workingHoursService.validateWorkingHours(dto.getUserId(), dto.getStartTime(), dto.getEndTime());

        boolean busy = orderRepo.existsOverlappingActiveOrder(
                dto.getUserId(),
                dto.getStartTime(),
                dto.getEndTime()
        );
        if (busy) {
            throw new TimeSlotBusyException();
        }

        User user = userService.findById(dto.getUserId());
        Customer customer = customerService.findById(dto.getCustomerId());
        List<UserWork> userWorks = userWorkService.findAllByIdIn(dto.getUserWorksId());

        double price = userWorks.stream().mapToDouble(UserWork::getPrice).sum();

        Order order = new Order();
        order.setStatus(OrderStatus.ACTIVE);
        order.setUser(user);
        order.setCustomer(customer);
        order.setUserWorks(userWorks);
        order.setDate(new Date());
        order.setStartTime(dto.getStartTime());
        order.setEndTime(dto.getEndTime());
        order.setPrice(price);

        Order saved = orderRepo.save(order);

        response.setObject(saved);
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
