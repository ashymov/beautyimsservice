package com.sashymov.beautyimsservice.services;

import com.sashymov.beautyimsservice.models.dto.UserDto;
import com.sashymov.beautyimsservice.models.entities.User;
import com.sashymov.beautyimsservice.respones.Response;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    Response getUserByUsername(String username);
    User getUserByEmail(String email);
    Response getUserByPhone(String phone);
    Response saveUser(UserDto userDto);
    User findById(Long id);
    Response upload(MultipartFile multipartFile, Long userId);


}
