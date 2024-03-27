package com.chris.ims.entity;

import lombok.Getter;

/**
 * DTO for {@link SpecEntity}
 */
@Getter
public abstract class SpecEntityDto extends AbstractEntityDto {
  String code;
  String name;

  protected SpecEntityDto(SpecEntity entity) {
    super(entity);
    this.code = entity.getCode();
    this.name = entity.getName();
  }
}