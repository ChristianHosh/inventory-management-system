package com.chris.ims.entity.exception;

import org.springframework.http.HttpStatus;

public class CException extends RuntimeException {

  public CException(String message) {
    super(message);
  }

  public CException(String message, Throwable cause) {
    super(message, cause);
  }

  public HttpStatus getStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
