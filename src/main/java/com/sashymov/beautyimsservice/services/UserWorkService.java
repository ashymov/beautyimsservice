package com.sashymov.beautyimsservice.services;

import com.sashymov.beautyimsservice.models.dto.CreateUserWorkDto;
import com.sashymov.beautyimsservice.models.entities.UserWork;
import com.sashymov.beautyimsservice.respones.Response;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserWorkService {
    Response findByName(String userWorkName);

    Response save(CreateUserWorkDto createUserWorkDto);

    Response findAll();

    Response findAllByUserId(Long userId);

    Response findByPriceLessThan(Double price);

    Response findByPriceBetween(Double minPrice, Double maxPrice);

    Response findByPriceGreaterThan(Double price);
    Response upload(MultipartFile multipartFile, Long userWorkId);
    List<UserWork> findAllByIdIn(List<Long> ids);

}