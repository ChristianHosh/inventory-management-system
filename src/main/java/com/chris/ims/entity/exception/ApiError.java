package com.chris.ims.entity.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiError(
        LocalDateTime timestamp,
        String error,
        String message,
        Integer status,
        Boolean isSevere

) {

  public ApiError(HttpStatus status, String message, Boolean isSevere) {
    this(LocalDateTime.now(), status.getReasonPhrase(), message, status.value(), isSevere);
  }
}