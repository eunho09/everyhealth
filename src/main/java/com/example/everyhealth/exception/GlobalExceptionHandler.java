package com.example.everyhealth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.warn("Business Exception: {} | ErrorCode: {} | Data: {}",
                e.getMessage(), e.getErrorCode(), Arrays.toString(e.getArgs()));

        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = determineHttpStatus(errorCode);

        return ResponseEntity.status(status)
                .body(ErrorResponse.of(errorCode.getCode(), errorCode.getMessage()));
    }

    private HttpStatus determineHttpStatus(ErrorCode errorCode) {

        return switch (errorCode.getCode()) {
            case "400" -> HttpStatus.BAD_REQUEST;
            case "403" -> HttpStatus.FORBIDDEN;
            case "404" -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.BAD_REQUEST;
        };
    }

}
