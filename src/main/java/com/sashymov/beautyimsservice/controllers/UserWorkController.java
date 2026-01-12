package com.sashymov.beautyimsservice.controllers;

import com.sashymov.beautyimsservice.models.dto.CreateUserWorkDto;
import com.sashymov.beautyimsservice.models.entities.UserWork;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.UserWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;



@RestController
@RequestMapping("/api/v1/userWork")
public class UserWorkController {
    @Autowired
    private UserWorkService userWorkService;

    @PostMapping("/createUserWork")
    public Response createUserWork(@RequestBody CreateUserWorkDto createUserWorkDto) {
        return userWorkService.save(createUserWorkDto);
    }

    @GetMapping("/getByName")
    public Response getByName(@RequestParam String userWorkName) {
        return userWorkService.findByName(userWorkName);
    }

    @GetMapping("/getAll")
    public Response getAll() {
        return userWorkService.findAll();
    }
    @GetMapping("/getAllByUser")
    public Response getAllByUser(@RequestParam Long userId) {
        return userWorkService.findAllByUserId(userId);
    }

    @GetMapping("/getByPriceLessThan")
    public Response getByPriceLessThan(@RequestParam Double price) {
        return userWorkService.findByPriceLessThan(price);
    }

    @GetMapping("/getByPriceBetween")
    public Response getByPriceBetween(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        return userWorkService.findByPriceBetween(minPrice, maxPrice);
    }
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public Response upload(@RequestPart MultipartFile file,@RequestParam Long userWorkId) {
        return userWorkService.upload(file,userWorkId);
    }

}
