package com.sashymov.beautyimsservice.models.dto;

import com.sashymov.beautyimsservice.enums.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDto {
    private int price;
    private OrderStatus status;
    private Long userId;
    private List<Long> userWorksId;
    private Long customerId;
}
