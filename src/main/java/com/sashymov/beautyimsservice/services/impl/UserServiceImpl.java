package com.sashymov.beautyimsservice.services.impl;

import com.sashymov.beautyimsservice.dao.UserRepo;
import com.sashymov.beautyimsservice.microservices.fileService.FileResponse;
import com.sashymov.beautyimsservice.microservices.fileService.FileServiceFeign;
import com.sashymov.beautyimsservice.models.dto.UserDto;
import com.sashymov.beautyimsservice.models.entities.Company;
import com.sashymov.beautyimsservice.models.entities.File;
import com.sashymov.beautyimsservice.models.entities.User;
import com.sashymov.beautyimsservice.models.entities.UserWork;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.CompanyService;
import com.sashymov.beautyimsservice.services.FileService;
import com.sashymov.beautyimsservice.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo  userRepo;
    private final CompanyService companyService;
    private final FileServiceFeign fileServiceFeign;
    private final FileService fileService;


    public UserServiceImpl(UserRepo userRepo, CompanyService companyService, FileServiceFeign fileServiceFeign, FileService fileService) {
        this.userRepo = userRepo;
        this.companyService = companyService;
        this.fileServiceFeign = fileServiceFeign;
        this.fileService = fileService;
    }

    @Override
    public Response getUserByUsername(String username) {
        Response response = Response.getResponse();

        User user = userRepo.findByName( username);
        response.setObject(user);
        return response;
    }

    @Override
    public User getUserByEmail(String email) {

        return userRepo.findByEmail(email);
    }

    @Override
    public Response getUserByPhone(String phone) {
        Response response = Response.getResponse();
        User user = userRepo.findByPhone(phone);
        response.setObject(user);

        return response;
    }



    @Override
    public Response saveUser(UserDto userDto) {
        Response response = Response.getResponse();

        User user = getUserByEmail(userDto.getEmail());
        if (user == null) {
            Company company = companyService.findById(userDto.getCompany().getCompanyId());
            user = new  User();
            user.setName( userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setPhone(userDto.getPhone());
            user.setCompany(company);
            userRepo.save(user);
            response.setObject(user);
            return response;
        }
        response.setStatus(0);
        response.setMessage("User already exists");
        return response;
    }

    @Override
    public User findById(Long id) {
        User user = userRepo.findById(id).orElse(null);
        return user;
    }

    @Override
    public Response upload(MultipartFile multipartFile, Long userId) {
        Response response = Response.getResponse();
        User user =  userRepo.findById(userId).orElse(null);
        if (user == null) {
            response.setMessage("UserWork Not Found");
            response.setStatus(0);
            return response;
        }
        FileResponse fileResponse = fileServiceFeign.upload(multipartFile);
        File file = new File();
        file.setFileDownloadUri(fileResponse.getDownloadUri());
        file.setFileName(fileResponse.getFileName());
        file.setFileType(fileResponse.getFileType());
        file.setSize(fileResponse.getSize());
        file = fileService.save(file);
        user.setFile(file);
        userRepo.save(user);
        response.setObject(user);



        return response;
    }
}
