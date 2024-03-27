package com.chris.ims.entity.exception;

class BxSevereException extends BxException {

  protected BxSevereException(String message) {
    super(message);
  }

  protected BxSevereException(String message, Exception cause) {
    super(message, cause);
  }

}
