package com.sashymov.beautyimsservice.microservices.fileService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "${micro.file-service.name}",url = "${micro.file-service.url}")
public interface FileServiceFeign {
    @PostMapping(value = "/api/v1/file/upload", consumes = "multipart/form-data")
    FileResponse upload(@RequestPart MultipartFile multipartFile);

    @GetMapping("/download/{folder}/{fileName}")
    ResponseEntity<Resource> downloadFile(@PathVariable String folder, @PathVariable String fileName, HttpServletRequest request);
}