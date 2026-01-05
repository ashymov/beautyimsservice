package com.sashymov.beautyimsservice.dao;

import com.sashymov.beautyimsservice.models.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepo extends JpaRepository<File, Long> {
}
