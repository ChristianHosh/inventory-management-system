package com.chris.ims.entity;

import com.chris.ims.entity.annotations.Keyword;
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

}