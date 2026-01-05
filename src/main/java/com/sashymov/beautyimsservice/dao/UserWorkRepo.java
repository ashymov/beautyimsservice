package com.sashymov.beautyimsservice.dao;

import com.sashymov.beautyimsservice.models.entities.UserWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWorkRepo extends JpaRepository<UserWork,Long> {
    UserWork findByName(String name);

    List<UserWork> findAllByUserId(Long userId);
    List<UserWork> findByPriceGreaterThan(Double price);

    List<UserWork> findByPriceLessThan(Double price);
    List<UserWork> findByPriceBetween(Double minPrice, Double maxPrice);
    List<UserWork> findAllByIdIn(List<Long> ids);

}
