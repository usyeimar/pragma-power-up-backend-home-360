package com.pragma.home360.home.shared.configurations.beans;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, Integer.class, source -> {
            if (source.equals("null")) {
                return null;
            }
            return Integer.parseInt(source);
        });
    }
}