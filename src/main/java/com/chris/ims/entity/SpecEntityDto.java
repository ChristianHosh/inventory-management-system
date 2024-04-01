package com.chris.ims.entity;

import lombok.Getter;

/**
 * Response DTO for {@link SpecEntity}
 */
@Getter
public abstract class SpecEntityDto extends AbstractEntityDto {
  String name;

  protected SpecEntityDto(SpecEntity entity) {
    super(entity);
    this.name = entity.getName();
  }
}