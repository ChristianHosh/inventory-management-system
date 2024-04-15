package com.chris.ims.entity.exception;

import org.springframework.http.HttpStatus;

class CxBadRequestException extends CxException {
  protected CxBadRequestException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.BAD_REQUEST;
  }
}
