package com.example.everyhealth.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface FileStorageService {

    ArrayList<String> ALLOW_EXT = new ArrayList<>(List.of("jpg", "jpeg", "png", "gif"));

    String uploadFile(MultipartFile file);

    String generateUniqueFileName(String originalFilename);

    String extractExt(String originName);

    Object downloadFile(String key) throws MalformedURLException;

    default boolean isValid(String fileName){
        String ext = extractExt(fileName).toLowerCase();
        return ALLOW_EXT.contains(ext);
    }
}
