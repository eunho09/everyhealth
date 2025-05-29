package com.example.everyhealth.exception;

public class MemberException extends BusinessException{
    public MemberException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
