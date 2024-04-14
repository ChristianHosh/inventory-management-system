package com.chris.ims.entity;

import com.chris.ims.entity.annotations.SubEntityList;
import com.chris.ims.entity.exception.BxException;
import com.chris.ims.entity.utils.CResources;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Getter
public class EntField {

  private final DbTable table;
  private final String fieldName;
  private final Field field;
  private final Class<?> type;
  private final int id;

  private final Set<Annotation> annotations;

  EntField(DbTable table, Field field, int id) {
    field.trySetAccessible();

    this.table = table;
    this.field = field;
    this.id = id;
    this.fieldName = field.getName();
    this.type = field.getType();
    this.annotations = Set.of(field.getDeclaredAnnotations());
  }

  boolean isSubEntity() {
    return field.isAnnotationPresent(SubEntityList.class) && (Iterable.class.isAssignableFrom(field.getType()));
  }

  public String getLabel() {
    return CResources.getValue(id);
  }

  public boolean isField(int... ids) {
    for (int i : ids) {
      if (i == this.id) {
        return true;
      }
    }
    return false;
  }

  public Object get(AbstractEntity entity) {
    try {
      return field.get(entity);
    } catch (IllegalAccessException e) {
      log.error("error getting field [" + this + "] " + e.getMessage(), e);
      throw BxException.unexpected(e);
    }
  }

  public void set(AbstractEntity entity, Object value) {
    setInternal(entity, value);
  }

  private void setInternal(AbstractEntity entity, Object value) {
    Object oldValue = get(entity);
    if (Objects.equals(oldValue, value))
      return;

    entity.validate(this);
    entity.fieldChanged(this, value, oldValue);
    try {
      field.set(entity, value);
    } catch (IllegalAccessException e) {
      log.error("error setting field [" + this + "] " + e.getMessage(), e);
      throw BxException.unexpected(e);
    }
  }

  public boolean isColumn() {
    return field.isAnnotationPresent(Column.class);
  }

  public Column getColumn() {
    return field.getAnnotation(Column.class);
  }

  public boolean isJoinColumn() {
    return field.isAnnotationPresent(JoinColumn.class);
  }

  public JoinColumn getJoinColumn() {
    return field.getAnnotation(JoinColumn.class);
  }
}
