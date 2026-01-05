package com.sashymov.beautyimsservice.microservices.fileService;

import lombok.Data;

@Data
public class FileResponse {

    private String fileName;
    private String downloadUri;
    private String fileType;
    private long size;

    public FileResponse(String fileName, String downloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.downloadUri = downloadUri;
        this.fileType = fileType;
        this.size = size;
    }
}