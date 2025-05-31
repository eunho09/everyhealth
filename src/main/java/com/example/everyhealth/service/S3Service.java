package com.example.everyhealth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@Profile("prod")
public class S3Service implements FileStorageService {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(S3Client s3Client, @Value("${app.aws.s3.bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public String uploadFile(MultipartFile file){
        if (file.isEmpty()){
            return null;
        }

        if (!isValid(file.getOriginalFilename())){
            throw new RuntimeException("파일 확장자가 일치하지 않습니다.");
        }

        try {
            String key = "images/" + generateUniqueFileName(file.getOriginalFilename());

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return key;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] downloadFile(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try (var objectResponse = s3Client.getObject(getObjectRequest)) {
            return objectResponse.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("파일 다운로드 실패", e);
        }
    }

    @Override
    public String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID() + "." + extractExt(originalFilename);
    }

    @Override
    public String extractExt(String originName) {
        int i = originName.lastIndexOf(".");
        return originName.substring(i + 1);
    }
}
