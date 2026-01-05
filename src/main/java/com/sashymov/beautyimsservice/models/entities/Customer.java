package com.sashymov.beautyimsservice.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String phone;
    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;
}
