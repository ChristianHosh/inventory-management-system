package com.chris.ims.entity.exception;

import org.springframework.http.HttpStatus;

class BxConflictException extends BxException {
  protected BxConflictException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.CONFLICT;
  }
}
