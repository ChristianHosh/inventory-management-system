package com.chris.ims.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

@MappedSuperclass
public abstract class SubEntity extends AbstractEntity {

  @Transient
  public abstract AbstractEntity getParent();

}
