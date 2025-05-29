package com.example.everyhealth.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {

    private String code;
    private String message;
    private Object data;
    private LocalDateTime timestamp;
    private String path;

    public static ErrorResponse of(String code, String message){
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
