package com.sashymov.beautyimsservice.dao;

import com.sashymov.beautyimsservice.models.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Long> {
    Customer findByName(String name);

    Customer findByEmail(String email);

    Customer findByPhone(String phone);
    List<Customer> findAll();
    Customer save(Customer customer);
}
