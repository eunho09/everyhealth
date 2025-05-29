package com.example.everyhealth.exception;

public class FriendException extends BusinessException{
    public FriendException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
