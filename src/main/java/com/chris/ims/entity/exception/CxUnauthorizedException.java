package com.chris.ims.entity.exception;

import org.springframework.http.HttpStatus;

class CxUnauthorizedException extends CxException {
  public CxUnauthorizedException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.UNAUTHORIZED;
  }
}
