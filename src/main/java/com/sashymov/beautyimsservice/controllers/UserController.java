package com.sashymov.beautyimsservice.controllers;

import com.sashymov.beautyimsservice.models.dto.UserDto;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.FileService;
import com.sashymov.beautyimsservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @PostMapping("/create")
    public Response createUser(@RequestBody UserDto userDto) {
       return userService.saveUser(userDto);
    }
}
