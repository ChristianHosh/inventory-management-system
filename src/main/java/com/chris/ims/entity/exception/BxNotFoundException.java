package com.chris.ims.entity.exception;

import org.springframework.http.HttpStatus;

class BxNotFoundException extends BxException {
  protected BxNotFoundException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
