package com.example.everyhealth.exception;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

  private final ErrorCode errorCode;
  private final Object[] args;

  protected BusinessException(ErrorCode errorCode, Object... args) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.args = args;
  }

}
