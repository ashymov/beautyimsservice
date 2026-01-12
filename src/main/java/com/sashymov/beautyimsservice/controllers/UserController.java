package com.sashymov.beautyimsservice.controllers;

import com.sashymov.beautyimsservice.models.dto.UserDto;
import com.sashymov.beautyimsservice.models.entities.User;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.FileService;
import com.sashymov.beautyimsservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
    @GetMapping("/getByUsername")
    public Response getUserByUsername(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/getByEmail")
    public User getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/getUserByPhone")
    public Response getUserByPhone(@RequestParam String phone) {
        return userService.getUserByPhone(phone);
    }

    @GetMapping("/getById")
    public User getById(@RequestParam Long id) {
        return userService.findById(id);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public Response upload(@RequestPart MultipartFile file,@RequestParam Long userId) {
        return userService.upload(file,userId);
    }
}
