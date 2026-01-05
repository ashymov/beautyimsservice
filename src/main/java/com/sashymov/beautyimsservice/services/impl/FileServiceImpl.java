package com.sashymov.beautyimsservice.services.impl;

import com.sashymov.beautyimsservice.dao.FileRepo;
import com.sashymov.beautyimsservice.models.entities.File;
import com.sashymov.beautyimsservice.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {
    private final FileRepo fileRepo;

    public FileServiceImpl(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }

    @Override
    public File save(File file) {
        fileRepo.save(file);
        return file;
    }
}
