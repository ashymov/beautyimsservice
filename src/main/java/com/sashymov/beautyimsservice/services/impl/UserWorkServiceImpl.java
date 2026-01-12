package com.sashymov.beautyimsservice.services.impl;

import com.sashymov.beautyimsservice.dao.UserWorkRepo;
import com.sashymov.beautyimsservice.microservices.fileService.FileResponse;
import com.sashymov.beautyimsservice.microservices.fileService.FileServiceFeign;
import com.sashymov.beautyimsservice.models.dto.CreateUserWorkDto;
import com.sashymov.beautyimsservice.models.entities.File;
import com.sashymov.beautyimsservice.models.entities.UserWork;
import com.sashymov.beautyimsservice.respones.Response;
import com.sashymov.beautyimsservice.services.FileService;
import com.sashymov.beautyimsservice.services.UserService;
import com.sashymov.beautyimsservice.services.UserWorkService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public class UserWorkServiceImpl implements UserWorkService {

    private final UserWorkRepo userWorkRepo;
    private final UserService userService;
    private final FileServiceFeign fileServiceFeign;
    private final FileService fileService;

    public UserWorkServiceImpl(UserWorkRepo userWorkRepo, UserService userService, FileServiceFeign fileServiceFeign, FileService fileService) {
        this.userWorkRepo = userWorkRepo;
        this.userService = userService;
        this.fileServiceFeign = fileServiceFeign;
        this.fileService = fileService;
    }

    @Override
    public Response findByName(String userWorkName) {
        Response response = Response.getResponse();
       UserWork userWork =  userWorkRepo.findByName(userWorkName);
       response.setObject(userWork);
        return response;
    }

    @Override
    public Response save(CreateUserWorkDto createUserWorkDto) {
        Response response = Response.getResponse();

        UserWork userWork =  new UserWork();
        userWork.setName( createUserWorkDto.getName());
        userWork.setPrice( createUserWorkDto.getPrice());
        userWork.setUser(userService.findById(createUserWorkDto.getUserId()));

        userWorkRepo.save(userWork);
        response.setObject(userWork);
        return response;
    }

    @Override
    public Response findAll() {
        Response response = Response.getResponse();
        List<UserWork> userWorks = userWorkRepo.findAll();
        response.setObject(userWorks);
        return response;
    }

    @Override
    public Response findAllByUserId(Long userId) {
        Response response = Response.getResponse();
        List<UserWork> userWorks = userWorkRepo.findAllByUserId(userId);
        response.setObject(userWorks);
        return response;
    }

    @Override
    public Response findByPriceLessThan(Double price) {
        Response response = Response.getResponse();
        List<UserWork> userWorks = userWorkRepo.findByPriceLessThan(price);
        response.setObject(userWorks);
        return response;
    }

    @Override
    public Response findByPriceBetween(Double minPrice, Double maxPrice) {
        Response response = Response.getResponse();
        List<UserWork> userWorks = userWorkRepo.findByPriceBetween(minPrice, maxPrice);
        response.setObject(userWorks);
        return response;
    }

    @Override
    public Response findByPriceGreaterThan(Double price) {
        Response response = Response.getResponse();
        List<UserWork> userWorks = userWorkRepo.findByPriceGreaterThan(price);
        response.setObject(userWorks);
        return response;
    }

    @Override
    public Response upload(MultipartFile file, Long userWorkId) {
        Response response = Response.getResponse();
        UserWork userWork =  userWorkRepo.findById(userWorkId).orElse(null);
        if (userWork == null) {
            response.setMessage("UserWork Not Found");
            response.setStatus(0);
            return response;
        }
        FileResponse fileResponse = fileServiceFeign.upload(file);
        File file1 = new File();
        file1.setFileDownloadUri(fileResponse.getDownloadUri());
        file1.setFileName(fileResponse.getFileName());
        file1.setFileType(fileResponse.getFileType());
        file1.setSize(fileResponse.getSize());
        file1 = fileService.save(file1);
        userWork.setFile(file1);
        userWorkRepo.save(userWork);
        response.setObject(userWork);



        return response;
    }

    @Override
    public List<UserWork> findAllByIdIn(List<Long> ids) {
        return userWorkRepo.findAllByIdIn(ids);
    }


}
