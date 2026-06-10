package com.sashymov.beautyimsservice.controllers;

import com.sashymov.beautyimsservice.models.dto.UserDto;
import com.sashymov.beautyimsservice.models.entities.User;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public Response createUser(@RequestBody UserDto userDto) {
        return userService.saveUser(userDto);
    }

    @PutMapping("/update/{id}")
    public Response updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/delete/{id}")
    public Response deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/getById/{id}")
    public User getById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/getByUsername")
    public Response getUserByUsername(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/getByEmail")
    public Response getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/getByPhone")
    public Response getUserByPhone(@RequestParam String phone) {
        return userService.getUserByPhone(phone);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public Response upload(@RequestPart MultipartFile file, @RequestParam Long userId) {
        return userService.upload(file, userId);
    }
}