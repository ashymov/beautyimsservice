package com.sashymov.beautyimsservice.services.impl;

import com.sashymov.beautyimsservice.dao.UserRepo;
import com.sashymov.beautyimsservice.exceptions.NotFoundException;
import com.sashymov.beautyimsservice.microservices.fileService.FileResponse;
import com.sashymov.beautyimsservice.microservices.fileService.FileServiceFeign;
import com.sashymov.beautyimsservice.models.dto.UserDto;
import com.sashymov.beautyimsservice.models.entities.Company;
import com.sashymov.beautyimsservice.models.entities.File;
import com.sashymov.beautyimsservice.models.entities.User;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.CompanyService;
import com.sashymov.beautyimsservice.services.FileService;
import com.sashymov.beautyimsservice.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final CompanyService companyService;
    private final FileServiceFeign fileServiceFeign;
    private final FileService fileService;

    public UserServiceImpl(UserRepo userRepo, CompanyService companyService,
                           FileServiceFeign fileServiceFeign, FileService fileService) {
        this.userRepo = userRepo;
        this.companyService = companyService;
        this.fileServiceFeign = fileServiceFeign;
        this.fileService = fileService;
    }

    @Override
    public Response saveUser(UserDto userDto) {
        Response response = Response.getResponse();

        User existingUser = userRepo.findByEmail(userDto.getEmail());
        if (existingUser != null) {
            response.setStatus(0);
            response.setMessage("User already exists with email: " + userDto.getEmail());
            return response;
        }

        Company company = null;
        if (userDto.getCompany() != null && userDto.getCompany().getCompanyId() != null) {
            company = companyService.findById(userDto.getCompany().getCompanyId());
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setCompany(company);

        User savedUser = userRepo.save(user);

        response.setObject(savedUser);
        response.setMessage("User created successfully");
        return response;
    }

    @Override
    public Response updateUser(Long id, UserDto userDto) {
        Response response = Response.getResponse();

        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));

        if (!existingUser.getEmail().equals(userDto.getEmail())) {
            User userWithSameEmail = userRepo.findByEmail(userDto.getEmail());
            if (userWithSameEmail != null) {
                response.setStatus(0);
                response.setMessage("Email already exists: " + userDto.getEmail());
                return response;
            }
        }

        if (userDto.getCompany() != null && userDto.getCompany().getCompanyId() != null) {
            Company company = companyService.findById(userDto.getCompany().getCompanyId());
            existingUser.setCompany(company);
        }

        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPhone(userDto.getPhone());

        User updatedUser = userRepo.save(existingUser);

        response.setObject(updatedUser);
        response.setMessage("User updated successfully");
        return response;
    }

    @Override
    public Response deleteUser(Long id) {
        Response response = Response.getResponse();

        if (!userRepo.existsById(id)) {
            throw new NotFoundException("User", id);
        }

        userRepo.deleteById(id);
        response.setMessage("User deleted successfully");
        return response;
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
    }

    @Override
    public Response getUserByUsername(String username) {
        Response response = Response.getResponse();
        User user = userRepo.findByName(username);

        if (user == null) {
            response.setStatus(0);
            response.setMessage("User not found with username: " + username);
        } else {
            response.setObject(user);
            response.setMessage("User found");
        }
        return response;
    }

    @Override
    public Response getUserByEmail(String email) {
        Response response = Response.getResponse();
        User user = userRepo.findByEmail(email);

        if (user == null) {
            response.setStatus(0);
            response.setMessage("User not found with email: " + email);
        } else {
            response.setObject(user);
            response.setMessage("User found");
        }
        return response;
    }

    @Override
    public Response getUserByPhone(String phone) {
        Response response = Response.getResponse();
        User user = userRepo.findByPhone(phone);

        if (user == null) {
            response.setStatus(0);
            response.setMessage("User not found with phone: " + phone);
        } else {
            response.setObject(user);
            response.setMessage("User found");
        }
        return response;
    }

    @Override
    public Response upload(MultipartFile file, Long userId) {
        Response response = Response.getResponse();

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));

        try {
            FileResponse fileResponse = fileServiceFeign.upload(file);

            File fileEntity = new File();
            fileEntity.setFileDownloadUri(fileResponse.getDownloadUri());
            fileEntity.setFileName(fileResponse.getFileName());
            fileEntity.setFileType(fileResponse.getFileType());
            fileEntity.setSize(fileResponse.getSize());

            File savedFile = fileService.save(fileEntity);

            user.setFile(savedFile);
            userRepo.save(user);

            response.setObject(user);
            response.setMessage("File uploaded successfully");

        } catch (Exception e) {
            response.setStatus(0);
            response.setMessage("Error uploading file: " + e.getMessage());
        }

        return response;
    }
}