package com.chris.ims.entity.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiError implements Serializable {

  private final LocalDateTime timestamp;
  private final String error;
  private final String message;
  private final Integer status;
  private final Boolean isSevere;

  public ApiError(@NotNull HttpStatus status, String message, Boolean isSevere) {
    this(LocalDateTime.now(), status.getReasonPhrase(), message, status.value(), isSevere);
  }

  public ApiError(HttpStatus status, String message) {
    this(status, message, false);
  }
}