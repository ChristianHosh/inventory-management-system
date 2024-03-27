package com.chris.ims.entity.exception;

import org.springframework.http.HttpStatus;

class BxBadRequestException extends BxException {
  protected BxBadRequestException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.BAD_REQUEST;
  }
}
