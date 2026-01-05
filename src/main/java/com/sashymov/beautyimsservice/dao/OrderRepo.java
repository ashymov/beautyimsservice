package com.sashymov.beautyimsservice.dao;

import com.sashymov.beautyimsservice.models.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {
    List<Order> findAllByCustomerId(Long customerId);

    List<Order> findAllByCustomerName(String customerName);

    List<Order> findAllByCustomerEmail(String customerEmail);

    List<Order> findAllByCustomerPhone(String customerPhone);
}
