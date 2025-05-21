package com.pragma.home360.home.application.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    String storeFile(MultipartFile file, Long propertyId, String originalFilename) throws IOException;

    void deleteFile(String fileUrl) throws IOException;
}