package com.example.everyhealth.exception;

public class EmptyDataException extends BusinessException {
    public EmptyDataException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
