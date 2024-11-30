package com.example.everyhealth.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadPostDto {

    private MultipartFile file;
    private String text;
}
