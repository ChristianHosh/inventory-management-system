package com.chris.ims.entity.exception;

import org.springframework.http.HttpStatus;

class CxNotFoundException extends CxException {
  protected CxNotFoundException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
