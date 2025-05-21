package com.pragma.home360.home.application.services.impl;

import com.pragma.home360.home.application.services.FileStorageService;
import com.pragma.home360.home.domain.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path fileStorageLocation;
    private final String baseUrl;


    public LocalFileStorageService(@Value("${home360.file.upload-dir:./uploads/property-images}") String uploadDir,
                                   @Value("${home360.file.base-url:/media/properties}") String baseUrl) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.baseUrl = baseUrl;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file, Long propertyId, String originalFilenameProvided) throws IOException, IOException {
        if (file.isEmpty()) {
            throw new ValidationException("Failed to store empty file.");
        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = "";
        int i = originalFilename.lastIndexOf('.');
        if (i > 0) {
            extension = originalFilename.substring(i);
        }
        String uniqueFileName = UUID.randomUUID().toString() + extension;

        Path propertyDir = this.fileStorageLocation.resolve(String.valueOf(propertyId));
        Files.createDirectories(propertyDir); // Asegurar que el directorio de la propiedad exista

        Path targetLocation = propertyDir.resolve(uniqueFileName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        }

        return baseUrl + "/" + propertyId + "/" + uniqueFileName;
    }

    @Override
    public void deleteFile(String fileUrl) throws IOException {
        if (fileUrl == null || !fileUrl.startsWith(baseUrl)) {

            return;
        }
        String relativePath = fileUrl.substring(baseUrl.length());
        Path filePath = this.fileStorageLocation.resolve(relativePath.substring(1)).normalize();

        if (Files.exists(filePath) && filePath.startsWith(this.fileStorageLocation)) {
            Files.delete(filePath);
        } else {
            System.err.println("File to delete not found or path is invalid: " + filePath);
        }
    }
}