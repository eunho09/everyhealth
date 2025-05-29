package com.example.everyhealth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TodayException.class)
    public ResponseEntity<ErrorResponse> handleTodayExercise(TodayException e) {
        log.info("Today Exception : {} \n Data : {}", e.getMessage(), Arrays.toString(e.getArgs()));

        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(FriendException.class)
    public ResponseEntity<ErrorResponse> handleFriendException(FriendException e) {
        log.info("Friend Exception : {} \n Data : {}", e.getMessage(), Arrays.toString(e.getArgs()));

        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorResponse> handleMemberException(MemberException e) {
        log.info("Member Exception : {} \n Data : {}", e.getMessage(), Arrays.toString(e.getArgs()));

        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(errorCode.getCode(), errorCode.getMessage()));
    }

}
