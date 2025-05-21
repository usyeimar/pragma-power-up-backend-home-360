package com.pragma.home360.home.shared.configurations.beans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${home360.file.upload-dir:./uploads/property-images}")
    private String uploadDir;

    @Value("${home360.file.base-url:/media/properties}") // ej. /media/properties
    private String baseUrlPath;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, Integer.class, source -> {
            if (source.equals("null")) {
                return null;
            }
            return Integer.parseInt(source);
        });
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourcePath = baseUrlPath.endsWith("/") ? baseUrlPath + "**" : baseUrlPath + "/**";
        String resourceLocation = "file:" + Paths.get(uploadDir).toAbsolutePath().normalize().toString() + "/";

        registry.addResourceHandler(resourcePath)
                .addResourceLocations(resourceLocation);
    }
}