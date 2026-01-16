package com.sashymov.beautyimsservice.services.impl;

import com.sashymov.beautyimsservice.dao.OrderRepo;
import com.sashymov.beautyimsservice.dao.UserScheduleRepo;
import com.sashymov.beautyimsservice.enums.OrderStatus;
import com.sashymov.beautyimsservice.exceptions.*;
import com.sashymov.beautyimsservice.models.dto.CancelOrderDto;
import com.sashymov.beautyimsservice.models.dto.CreateOrderDto;
import com.sashymov.beautyimsservice.models.dto.RescheduleOrderDto;
import com.sashymov.beautyimsservice.models.entities.*;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

        if (dto.startTime() == null) {
                throw new InvalidTimeRangeException();
            }

            User user = userService.findById(dto.userId());
            Customer customer = customerService.findById(dto.customerId());
            List<UserWork> works = userWorkService.findAllByIdIn(dto.userWorksId());

            if (works.isEmpty()) {
                throw new BusinessException("EMPTY_WORKS", "At least one service required") {};
            }

            int totalMinutes = works.stream()
                    .mapToInt(UserWork::getDurationMinutes)
                    .sum();

            LocalDateTime endTime = dto.startTime().plusMinutes(totalMinutes);

            workingHoursService.validateWorkingHours(user.getId(), dto.startTime(), endTime);

            boolean busy = orderRepo.existsOverlappingActiveOrder(
                    user.getId(),
                    dto.startTime(),
                    endTime
            );
            if (busy) {
                throw new TimeSlotBusyException();
            }

            double price = works.stream().mapToDouble(UserWork::getPrice).sum();

            Order order = new Order();
            order.setUser(user);
            order.setCustomer(customer);
            order.setUserWorks(works);
            order.setStartTime(dto.startTime());
            order.setEndTime(endTime);
            order.setPrice(price);
            order.setStatus(OrderStatus.ACTIVE);
            order.setDate(new Date());

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

    @Override
    public Response cancel(Long orderId, CancelOrderDto dto) {
        Response response = Response.getResponse();

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order", orderId));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BusinessException("ORDER_COMPLETED", "Completed order cannot be canceled") {};
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("ORDER_ALREADY_CANCELED", "Order already canceled") {};
        }

        int cancelDeadlineMinutes = 60;
        if (order.getStartTime() != null) {
            LocalDateTime deadline = order.getStartTime().minusMinutes(cancelDeadlineMinutes);
            if (LocalDateTime.now().isAfter(deadline)) {
                throw new BusinessException("CANCEL_DEADLINE", "Too late to cancel") {};
            }
        }

        String reason = (dto == null || dto.reason() == null) ? null : dto.reason().trim();
        if (reason != null && reason.length() > 500) {
            throw new BusinessException("INVALID_REASON", "Reason too long") {};
        }

        int updated = orderRepo.cancelOrder(orderId, LocalDateTime.now(), reason);
        if (updated == 0) {
            throw new BusinessException("ORDER_NOT_CANCELABLE", "Order cannot be canceled") {};
        }

        Order canceled = orderRepo.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order", orderId));

        response.setObject(canceled);
        return response;
    }

    @Transactional
    @Override
    public Response reschedule(Long orderId, RescheduleOrderDto dto) {
        Response response = Response.getResponse();

        if (dto.newStartTime() == null) {
            throw new InvalidTimeRangeException();
        }

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order", orderId));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("ORDER_CANCELED", "Canceled order cannot be rescheduled") {};
        }
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BusinessException("ORDER_COMPLETED", "Completed order cannot be rescheduled") {};
        }

        int totalMinutes = order.getUserWorks().stream()
                .mapToInt(UserWork::getDurationMinutes)
                .sum();

        LocalDateTime newStart = dto.newStartTime();
        LocalDateTime newEnd   = newStart.plusMinutes(totalMinutes);

        workingHoursService.validateWorkingHours(
                order.getUser().getId(),
                newStart,
                newEnd
        );

        boolean busy = orderRepo.existsOverlappingActiveOrderExcluding(
                order.getUser().getId(),
                order.getId(),
                newStart,
                newEnd
        );
        if (busy) {
            throw new TimeSlotBusyException();
        }

        order.setStartTime(newStart);
        order.setEndTime(newEnd);

        orderRepo.save(order);

        response.setObject(order);
        return response;
    }
}
