package com.sashymov.beautyimsservice.models.entities;

import com.sashymov.beautyimsservice.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date date;
    private String time;
    private double price;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToMany(cascade = CascadeType.ALL)
    private List<UserWork>  userWorks;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
