package com.sashymov.beautyimsservice.services;

import com.sashymov.beautyimsservice.models.dto.UserDto;
import com.sashymov.beautyimsservice.models.entities.User;
import com.sashymov.beautyimsservice.respones.Response;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    Response saveUser(UserDto userDto);
    Response updateUser(Long id, UserDto userDto);
    Response deleteUser(Long id);
    List<User> findAll();
    User findById(Long id);

    Response getUserByUsername(String username);
    Response getUserByEmail(String email);
    Response getUserByPhone(String phone);

    Response upload(MultipartFile multipartFile, Long userId);
}