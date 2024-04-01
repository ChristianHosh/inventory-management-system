package com.chris.ims.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Response DTO for {@link AbstractEntity}
 */
@Getter
public abstract class AbstractEntityDto implements Serializable {
  private final Long id;
  private final LocalDateTime createdOn;
  private final LocalDateTime updatedOn;

  protected AbstractEntityDto(AbstractEntity entity) {
    this.id = entity.getId();
    this.createdOn = entity.getCreatedOn();
    this.updatedOn = entity.getUpdatedOn();
  }


}