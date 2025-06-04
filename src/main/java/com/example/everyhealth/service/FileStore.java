package com.example.everyhealth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

@Component
@Profile("!Prod")
public class FileStore implements FileStorageService {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullName(String fileName) {
        return fileDir + fileName;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        if (!isValid(file.getOriginalFilename())){
            throw new RuntimeException("파일 확장자가 일치하지 않습니다.");
        }

        String originalFilename = file.getOriginalFilename();
        String uniqueFileName = generateUniqueFileName(originalFilename);

        try {
            file.transferTo(new File(getFullName(uniqueFileName)));
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        return uniqueFileName;
    }

    @Override
    public String generateUniqueFileName(String originName) {
        return UUID.randomUUID() + "." + extractExt(originName);
    }

    @Override
    public String extractExt(String originName) {
        int i = originName.lastIndexOf(".");
        return originName.substring(i + 1).toLowerCase();
    }

    @Override
    public UrlResource downloadFile(String uniqueFileName) throws MalformedURLException {
        return new UrlResource("file:" + getFullName(uniqueFileName));
    }
}
