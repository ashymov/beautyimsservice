package com.sashymov.beautyimsservice.dao;

import com.sashymov.beautyimsservice.enums.CompanyStatus;
import com.sashymov.beautyimsservice.models.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long> {
    Company findByCompanyName(String companyName);

    Company findByCompanyEmail(String companyEmail);


    List<Company> findByCompanyStatus(CompanyStatus status);
}
