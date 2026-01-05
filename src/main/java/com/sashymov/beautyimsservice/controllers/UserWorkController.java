package com.sashymov.beautyimsservice.controllers;

import com.sashymov.beautyimsservice.models.dto.CreateUserWorkDto;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.UserWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user-work")
public class UserWorkController {
    @Autowired
    private UserWorkService userWorkService;

    @PostMapping("/createUserWork")
    public Response createUserWork(@RequestBody CreateUserWorkDto createUserWorkDto) {
        return userWorkService.save(createUserWorkDto);
    }
}
