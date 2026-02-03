package com.aloha.project.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//파일 이미지 제공 컨트롤러

@Controller
public class FileController {

    @GetMapping("/uploads/{filename: .+}")
    public ResponseEntity<Resource> getFile(@PathVariable("filename") String filename) throws MalformedURLException {
        Path file = Paths.get("C:/upload/").resolve(filename);
        Resource resource = new UrlResource(file.toUri());
        return ResponseEntity.ok().body(resource);
    }


}
