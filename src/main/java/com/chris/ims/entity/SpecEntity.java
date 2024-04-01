package com.chris.ims.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class SpecEntity extends AbstractEntity {

  @Keyword
  @Column(name = "name")
  private String name;

  @SuppressWarnings("unchecked")
  public <T extends SpecEntity> T setName(String name) {
    this.name = name;
    return (T) this;
  }

}