package com.chris.ims.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Response DTO for {@link AbstractEntity}
 */
@Getter
public abstract class AbstractEntityDto implements Serializable {

    /**
     * The unique identifier of the entity
     */
    private final Long id;

    /**
     * The timestamp when the entity was created
     */
    private final LocalDateTime createdOn;

    /**
     * The timestamp when the entity was last updated
     */
    private final LocalDateTime updatedOn;

    /**
     * Creates a new DTO instance from the given entity
     *
     * @param entity the entity to create the DTO from
     */
    protected AbstractEntityDto(AbstractEntity entity) {
        this.id = entity.getId();
        this.createdOn = entity.getCreatedOn();
        this.updatedOn = entity.getUpdatedOn();
    }

}