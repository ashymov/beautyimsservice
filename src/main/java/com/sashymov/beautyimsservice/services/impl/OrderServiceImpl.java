package com.sashymov.beautyimsservice.services.impl;

import com.sashymov.beautyimsservice.dao.OrderRepo;
import com.sashymov.beautyimsservice.dao.UserScheduleRepo;
import com.sashymov.beautyimsservice.enums.OrderStatus;
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

    @Transactional
    @Override
    public Response save(CreateOrderDto dto) {
        Response response = Response.getResponse();

        // 1) Базовая валидация времени
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new RuntimeException("StartTime/EndTime is required");
        }
        if (!dto.getEndTime().isAfter(dto.getStartTime())) {
            throw new RuntimeException("End time must be after start time");
        }

        // 2) Проверка рабочих часов (как у тебя)
        workingHoursService.validateWorkingHours(
                dto.getUserId(),
                dto.getStartTime(),
                dto.getEndTime()
        );

        // 3) Проверка занятости
        boolean busy = userScheduleRepo.existsOverlappingSlot(
                dto.getUserId(),
                dto.getStartTime(),
                dto.getEndTime()
        );
        if (busy) {
            throw new RuntimeException("Time slot is busy");
        }

        // 4) Достаём всё один раз
        User user = userService.findById(dto.getUserId());
        Customer customer = customerService.findById(dto.getCustomerId());
        List<UserWork> userWorks = userWorkService.findAllByIdIn(dto.getUserWorksId());

        double price = userWorks.stream().mapToDouble(UserWork::getPrice).sum();

        // 5) Создаём и сохраняем Order (ВАЖНО: сохраняем start/end)
        Order order = new Order();
        order.setStatus(OrderStatus.ACTIVE);
        order.setUser(user);
        order.setCustomer(customer);
        order.setUserWorks(userWorks);
        order.setDate(new Date());
        order.setStartTime(dto.getStartTime());
        order.setEndTime(dto.getEndTime());
        order.setPrice(price);

        order = orderRepo.save(order);

        // 6) Создаём слот (можно связать с order, если поле есть)
        UserSchedule slot = new UserSchedule();
        slot.setUser(user);
        slot.setStartTime(dto.getStartTime());
        slot.setEndTime(dto.getEndTime());
        // slot.setOrder(order); // если добавишь связь в сущность

        userScheduleRepo.save(slot);

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
