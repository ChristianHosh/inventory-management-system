package com.chris.ims.entity;

import com.chris.ims.entity.annotations.Parent;
import io.swagger.v3.core.util.ReflectionUtils;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

import java.lang.reflect.Field;
import java.util.List;

@MappedSuperclass
public abstract class SubEntity extends AbstractEntity {

  @Transient
  public AbstractEntity getParent() {
    List<Field> fieldList = ReflectionUtils.getDeclaredFields(getClass())
            .stream()
            .filter(field -> field.isAnnotationPresent(Parent.class))
            .toList();
    if (fieldList.isEmpty())
      throw new IllegalStateException("no parent entity found, declare the parent with @Parent annotation");
    if (fieldList.size() >= 2)
      throw new IllegalStateException("multiple parent entities found, only one parent entity can be declared");
    Field field = fieldList.getFirst();
    field.setAccessible(true);
    try {
      return (AbstractEntity) field.get(this);
    } catch (IllegalAccessException e) {
      return null;
    }
  }

}
