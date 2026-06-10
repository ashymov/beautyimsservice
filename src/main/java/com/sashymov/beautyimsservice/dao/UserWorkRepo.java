package com.sashymov.beautyimsservice.dao;

import com.sashymov.beautyimsservice.models.entities.UserWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWorkRepo extends JpaRepository<UserWork, Long> {
    UserWork findByName(String name);

    @Query("SELECT uw FROM UserWork uw LEFT JOIN FETCH uw.user")
    List<UserWork> findAllWithUser();

    @Query("SELECT uw FROM UserWork uw LEFT JOIN FETCH uw.user WHERE uw.user.id = :userId")
    List<UserWork> findAllByUserId(@Param("userId") Long userId);

    List<UserWork> findByPriceGreaterThan(Double price);
    List<UserWork> findByPriceLessThan(Double price);
    List<UserWork> findByPriceBetween(Double minPrice, Double maxPrice);
    List<UserWork> findAllByIdIn(List<Long> ids);
}