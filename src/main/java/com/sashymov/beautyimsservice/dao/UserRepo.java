package com.sashymov.beautyimsservice.dao;

import com.sashymov.beautyimsservice.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    User findByName(String name);

    User findByEmail(String email);

    User findByPhone(String phone);
}
