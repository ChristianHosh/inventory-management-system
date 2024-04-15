package com.chris.ims.entity;

import com.chris.ims.entity.annotations.SubEntityList;
import com.chris.ims.entity.exception.CxException;
import com.chris.ims.entity.utils.CResources;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Getter
@SuppressWarnings("unused")
public class EntField {

  private final DbTable table;
  private final String fieldName;
  private final Field field;
  private final Class<?> type;
  private final int id;

  private boolean enabled;
  private final List<BooleanFld> enablers;

  EntField(DbTable table, Field field, int id) {
    field.trySetAccessible();

    this.table = table;
    this.field = field;
    this.id = id;
    this.fieldName = field.getName();
    this.type = field.getType();
    this.enabled = true;
    this.enablers = new ArrayList<>();
  }

  boolean isSubEntity() {
    return field.isAnnotationPresent(SubEntityList.class) && (Iterable.class.isAssignableFrom(field.getType()));
  }

  public boolean isEnabled(AbstractEntity entity) {
    if (!enabled)
      return false;

    for (BooleanFld booleanFld : enablers) {
      if (!booleanFld.isTrue(entity))
        return false;
    }
    return true;
  }

  @SuppressWarnings("all")
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public void setEnabled(BooleanFld booleanFld) {
    this.enablers.add(booleanFld);
  }

  public void setEnabled(BooleanFld... booleanFlds) {
    this.enablers.addAll(Arrays.asList(booleanFlds));
  }

  public String getLabel() {
    return CResources.getValue(id);
  }

  public boolean isField(int id) {
    return this.id == id;
  }

  public boolean isField(int... ids) {
    for (int i : ids) {
      if (isField(i))
        return true;
    }
    return false;
  }

  public Object get(AbstractEntity entity) {
    try {
      return field.get(entity);
    } catch (IllegalAccessException e) {
      log.error("error getting field [" + this + "] " + e.getMessage(), e);
      throw CxException.unexpected(e);
    }
  }

  public void set(AbstractEntity entity, Object value) {
    setInternal(entity, value);
  }

  private void setInternal(AbstractEntity entity, Object value) {
    if (!isEnabled(entity)) {
      throw CxException.disabled(entity, this);
    }

    Object oldValue = get(entity);
    if (Objects.equals(oldValue, value))
      return;

    entity.validate(this);
    entity.fieldChanged(this, value, oldValue);
    try {
      this.field.set(entity, value);
    } catch (IllegalAccessException e) {
      log.error("error setting field [" + this + "] " + e.getMessage(), e);
      throw CxException.unexpected(e);
    }
  }

  public boolean isColumn() {
    return this.field.isAnnotationPresent(Column.class);
  }

  public Column getColumn() {
    return this.field.getAnnotation(Column.class);
  }

  public boolean isJoinColumn() {
    return this.field.isAnnotationPresent(JoinColumn.class);
  }

  public JoinColumn getJoinColumn() {
    return this.field.getAnnotation(JoinColumn.class);
  }

  @Override
  public String toString() {
    return "EntField: TABLE=" + table + ", FIELD=" + field + ", ID=" + id;
  }

  private BooleanFld exp(BooleanFld.Operation operation, Object value) {
    return new BooleanFld(this, operation, value);
  }

  public BooleanFld expIsNull() {
    return expEQ(null);
  }

  public BooleanFld expNotNull() {
    return expNE(null);
  }

  public BooleanFld expIn(Object... values) {
    return exp(BooleanFld.Operation.IN_LIST, Arrays.asList(values));
  }

  public BooleanFld expEQ(Object value) {
    return exp(BooleanFld.Operation.EQ, value);
  }

  public BooleanFld expNE(Object value) {
    return exp(BooleanFld.Operation.NE, value);
  }

  public BooleanFld expLT(Object value) {
    return exp(BooleanFld.Operation.LT, value);
  }

  public BooleanFld expLE(Object value) {
    return exp(BooleanFld.Operation.LE, value);
  }

  public BooleanFld expGT(Object value) {
    return exp(BooleanFld.Operation.GT, value);
  }

  public BooleanFld expGE(Object value) {
    return exp(BooleanFld.Operation.GE, value);
  }
}
