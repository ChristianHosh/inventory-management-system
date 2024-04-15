package com.chris.ims.entity.exception;

class CxSevereException extends CxException {

  protected CxSevereException(String message) {
    super(message);
  }

  protected CxSevereException(String message, Exception cause) {
    super(message, cause);
  }

}
