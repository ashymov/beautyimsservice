package com.sashymov.beautyimsservice.models.entities;

import com.sashymov.beautyimsservice.enums.CompanyStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "company_id")
    private Long companyId;
    private String companyName;
    private String companyAddress;
    private String companyEmail;
    private String companyPhone;
    private Date createdDate;
    private Date updatedDate;
    @Enumerated(EnumType.STRING)
    private CompanyStatus  companyStatus;
    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

}
