package com.aloha.project.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Value("${file.upload.path}")
    private String uploadPath;

    public String save(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        File dir = new File(uploadPath);
        if (!dir.exists()) dir.mkdirs();

        String savedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File saveFile = new File(uploadPath, savedName);

        file.transferTo(saveFile);

        return savedName; // DB에 저장할 값
    }
}
