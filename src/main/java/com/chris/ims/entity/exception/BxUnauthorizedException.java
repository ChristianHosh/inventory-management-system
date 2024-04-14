package com.chris.ims.entity.exception;

import org.springframework.http.HttpStatus;

class BxUnauthorizedException extends BxException {
  public BxUnauthorizedException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.UNAUTHORIZED;
  }
}
