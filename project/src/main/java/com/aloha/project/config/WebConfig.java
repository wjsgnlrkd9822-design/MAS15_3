package com.aloha.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 🔹 업로드 이미지
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///" + uploadPath);

        // 🔹 static/img는 Spring Boot 기본 매핑 사용 → /img/** 로 접근 가능
    }
}
