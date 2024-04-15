package com.chris.ims.entity.exception;

import com.chris.ims.entity.AbstractEntity;
import com.chris.ims.entity.EntField;
import com.chris.ims.entity.security.AuthFacade;
import com.chris.ims.entity.utils.CResources;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.function.Supplier;

@Getter
@Setter
@Accessors(chain = true)
@SuppressWarnings("unused")
public class CxException extends RuntimeException {

  public static final int X_INVALID = CResources.create("xInvalid");
  public static final int X_INVALID_MSG = CResources.create("xInvalidMessage");
  public static final int X_MISSING = CResources.create("xMissing");


  private final Exception exception;

  protected CxException(int id, Object... args) {
    super(new MessageFormat(CResources.getValue(id), AuthFacade.getCurrentLocale()).format(args));
    this.exception = null;
  }

  protected CxException(String message) {
    super(message);
    this.exception = null;
  }

  protected CxException(Throwable cause) {
    super(cause);
    if (cause instanceof Exception e)
      this.exception = e;
    else
      this.exception = null;
  }

  protected CxException(String message, Throwable cause) {
    super(message, cause);
    if (cause instanceof Exception e)
      this.exception = e;
    else
      this.exception = null;
  }

  @NotNull
  @Contract("null -> new")
  public static CxException unauthorized(Object value) {
    return new CxUnauthorizedException("you are unauthorized to do this action: [" + value + "]");
  }


  public Exception getException() {
    return Objects.requireNonNullElse(exception, this);
  }


  @NotNull
  @Contract("_, _ -> new")
  public static CxException notFound(@NotNull Class<?> clazz, @NotNull Object value) {
    return new CxNotFoundException(clazz.getSimpleName() + ": not found for [" + value + "]");
  }

  @NotNull
  @Contract(pure = true)
  public static Supplier<CxException> xNotFound(Class<?> clazz, Object value) {
    return () -> {
      throw CxException.notFound(clazz, value);
    };
  }

  @NotNull
  @Contract("_, _, _ -> new")
  public static CxException conflict(@NotNull Class<?> clazz, @NotNull Object field, @NotNull Object value) {
    return new CxConflictException(clazz.getSimpleName() + ": " + field + " already exists for [" + value + "]");
  }

  @NotNull
  @Contract(pure = true)
  public static Supplier<CxException> xConflict(Class<?> clazz, @NotNull Object field, @NotNull Object value) {
    return () -> {
      throw CxException.conflict(clazz, field, value);
    };
  }

  @NotNull
  @Contract("_, _, _ -> new")
  public static CxException badRequest(@NotNull Class<?> clazz, @NotNull Object field, @NotNull Object value) {
    return new CxBadRequestException(clazz.getSimpleName() + ": " + field + " [" + value + "]");
  }

  @NotNull
  @Contract("_, _ -> new")
  public static CxException badRequest(@NotNull Class<?> clazz, @NotNull Object value) {
    return new CxBadRequestException(clazz.getSimpleName() + ": [" + value + "]");
  }

  @NotNull
  @Contract(pure = true)
  public static Supplier<CxException> xBadRequest(Class<?> clazz, @NotNull Object field, @NotNull Object value) {
    return () -> {
      throw CxException.badRequest(clazz, field, value);
    };
  }

  @NotNull
  @Contract(pure = true)
  public static Supplier<CxException> xBadRequest(Class<?> clazz, @NotNull Object value) {
    return () -> {
      throw CxException.badRequest(clazz, value);
    };
  }

  @NotNull
  @Contract("_ -> new")
  public static CxException hardcoded(String message) {
    return new CxSevereException(message);
  }

  public static CxException hardcoded(String message, HttpStatus status) {
    return new CxSevereException(message) {
      @Override
      public HttpStatus getStatus() {
        return status;
      }
    };
  }

  @NotNull
  @Contract("_, _ -> new")
  public static CxException hardcoded(String message, Object value) {
    return new CxSevereException(message + ": [" + value + "]");
  }

  @NotNull
  @Contract(pure = true)
  public static Supplier<CxException> xHardcoded(String message) {
    return () -> {
      throw CxException.hardcoded(message);
    };
  }

  @NotNull
  @Contract("_ -> new")
  public static CxException unexpected(@NotNull Exception e) {
    return new CxSevereException(e.getMessage(), e);
  }

  // NEW EXCEPTIONS REWORK
  public static CxException missing(AbstractEntity entity, EntField field) {
    return new CxBadRequestException(CResources.getFormattedValue(X_MISSING, entity, field));
  }

  public static CxException disabled(AbstractEntity entity, EntField field) {
    return new CxBadRequestException(String.format("(%s) [%s] cannot be updated", entity.getLabel(), field.getLabel()));
  }

  public static CxException invalid(AbstractEntity entity, EntField field) {
    return new CxBadRequestException(CResources.getFormattedValue(X_INVALID, entity, field));
  }

  public static CxException invalid(AbstractEntity entity, EntField field, String message) {
    return new CxBadRequestException(CResources.getFormattedValue(X_INVALID_MSG, entity, field, message));
  }

  public HttpStatus getStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
