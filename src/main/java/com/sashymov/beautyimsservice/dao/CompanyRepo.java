package com.sashymov.beautyimsservice.dao;

import com.sashymov.beautyimsservice.models.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long> {
    Company findByCompanyName(String companyName);

    Company findByCompanyEmail(String companyEmail);
}
