package com.chris.ims.entity.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.function.Supplier;

@Getter
@Setter
@Accessors(chain = true)
@SuppressWarnings("unused")
public class BxException extends RuntimeException {

  private final Exception exception;

  protected BxException(String message) {
    super(message);
    this.exception = null;
  }

  protected BxException(Throwable cause) {
    super(cause);
    if (cause instanceof Exception e)
      this.exception = e;
    else
      this.exception = null;
  }

  protected BxException(String message, Throwable cause) {
    super(message, cause);
    if (cause instanceof Exception e)
      this.exception = e;
    else
      this.exception = null;
  }

  @NotNull
  @Contract("null -> new")
  public static BxException unauthorized(Object value) {
    return new BxUnauthorizedException("you are unauthorized to do this action: [" + value + "]");
  }

  public Exception getException() {
    return Objects.requireNonNullElse(exception, this);
  }


  @NotNull
  @Contract("_, _ -> new")
  public static BxException notFound(@NotNull Class<?> clazz, @NotNull Object value) {
    return new BxNotFoundException(clazz.getSimpleName() + ": not found for [" + value + "]");
  }

  @NotNull
  @Contract(pure = true)
  public static Supplier<BxException> xNotFound(Class<?> clazz, Object value) {
    return () -> {
      throw BxException.notFound(clazz, value);
    };
  }

  @NotNull
  @Contract("_, _, _ -> new")
  public static BxException conflict(@NotNull Class<?> clazz, @NotNull Object field, @NotNull Object value) {
    return new BxConflictException(clazz.getSimpleName() + ": " + field + " already exists for [" + value + "]");
  }

  @NotNull
  @Contract(pure = true)
  public static Supplier<BxException> xConflict(Class<?> clazz, @NotNull Object field, @NotNull Object value) {
    return () -> {
      throw BxException.conflict(clazz, field, value);
    };
  }

  @NotNull
  @Contract("_, _, _ -> new")
  public static BxException badRequest(@NotNull Class<?> clazz, @NotNull Object field, @NotNull Object value) {
    return new BxBadRequestException(clazz.getSimpleName() + ": " + field + " [" + value + "]");
  }

  @NotNull
  @Contract("_, _ -> new")
  public static BxException badRequest(@NotNull Class<?> clazz, @NotNull Object value) {
    return new BxBadRequestException(clazz.getSimpleName() + ": [" + value + "]");
  }

  @NotNull
  @Contract(pure = true)
  public static Supplier<BxException> xBadRequest(Class<?> clazz, @NotNull Object field, @NotNull Object value) {
    return () -> {
      throw BxException.badRequest(clazz, field, value);
    };
  }

  @NotNull
  @Contract(pure = true)
  public static Supplier<BxException> xBadRequest(Class<?> clazz, @NotNull Object value) {
    return () -> {
      throw BxException.badRequest(clazz, value);
    };
  }

  @NotNull
  @Contract("_ -> new")
  public static BxException hardcoded(String message) {
    return new BxSevereException(message);
  }

  public static BxException hardcoded(String message, HttpStatus status) {
    return new BxSevereException(message) {
      @Override
      public HttpStatus getStatus() {
        return status;
      }
    };
  }

  @NotNull
  @Contract("_, _ -> new")
  public static BxException hardcoded(String message, Object value) {
    return new BxSevereException(message + ": [" + value + "]");
  }

  @NotNull
  @Contract(pure = true)
  public static Supplier<BxException> xHardcoded(String message) {
    return () -> {
      throw BxException.hardcoded(message);
    };
  }

  @NotNull
  @Contract("_ -> new")
  public static BxException unexpected(@NotNull Exception e) {
    return new BxSevereException(e.getMessage(), e);
  }

  public HttpStatus getStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
