package com.sashymov.beautyimsservice.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private List<UserWork> userWorks;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    private String phone;
    private String email;
    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;
}
