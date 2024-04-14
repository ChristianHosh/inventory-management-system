package com.chris.ims.entity;

import com.chris.ims.entity.annotations.Keyword;
import com.chris.ims.entity.annotations.Res;
import com.chris.ims.entity.utils.Resource;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

@Getter(AccessLevel.NONE)
@Setter(AccessLevel.NONE)
@MappedSuperclass
public abstract class SpecEntity extends AbstractEntity {

  public static final int F_NAME = Resource.F_NAME;

  @Keyword
  @Column(name = "name", nullable = false)
  @Res("name")
  private String name;

  @Override
  public String getName() {
    return getString(F_NAME);
  }

  public void setName(String name) {
    setField(F_NAME, name);
  }
}