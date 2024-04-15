package com.chris.ims.entity;

import java.util.Objects;

public class BooleanFld extends EntField {

  public enum Operation {
    EQ,
    NE,
    GT,
    GE,
    LT,
    IN_LIST, LE
  }

  private final EntField field;
  private final Operation operation;
  private final Object value;


  BooleanFld(EntField field, Operation operation, Object value) {
    super(field.getTable(), field.getField(), field.getId());
    this.field = field;
    this.operation = operation;
    this.value = value;
  }

  public boolean isTrue(AbstractEntity entity) {
    return internalIsTrue(entity);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private boolean internalIsTrue(AbstractEntity entity) {
    Object fieldValue = this.field.get(entity);
    if (fieldValue instanceof Boolean bool) {
      return bool;
    }

    return switch (operation) {
      case EQ -> Objects.equals(value, fieldValue);
      case NE -> !Objects.equals(value, fieldValue);
      case GT -> {
        if (fieldValue instanceof Comparable comparable && value instanceof Comparable oComparable) {
          yield comparable.compareTo(oComparable) > 0;
        }
        yield false;
      }
      case GE -> {
        if (fieldValue instanceof Comparable comparable && value instanceof Comparable oComparable) {
          yield comparable.compareTo(oComparable) >= 0;
        }
        yield false;
      }
      case LT -> {
        if (fieldValue instanceof Comparable comparable && value instanceof Comparable oComparable) {
          yield comparable.compareTo(oComparable) < 0;
        }
        yield false;
      }
      case LE -> {
        if (fieldValue instanceof Comparable comparable && value instanceof Comparable oComparable) {
          yield comparable.compareTo(oComparable) <= 0;
        }
        yield false;
      }
      case IN_LIST -> {
        if (value instanceof Iterable iterable) {
          for (Object o : iterable) {
            if (Objects.equals(o, fieldValue)) {
              yield true;
            }
          }
        }
        yield false;
      }
    };
  }

}
