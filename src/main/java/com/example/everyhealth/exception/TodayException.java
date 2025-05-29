package com.example.everyhealth.exception;

public class TodayException extends BusinessException{
    public TodayException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
