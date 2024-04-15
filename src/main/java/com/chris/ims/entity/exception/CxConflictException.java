package com.chris.ims.entity.exception;

import org.springframework.http.HttpStatus;

class CxConflictException extends CxException {
  protected CxConflictException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.CONFLICT;
  }
}
