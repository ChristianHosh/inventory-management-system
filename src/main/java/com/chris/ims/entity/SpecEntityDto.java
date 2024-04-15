package com.chris.ims.entity;

import lombok.Getter;

/**
 * Response DTO for {@link SpecEntity}
 */
@Getter
public abstract class SpecEntityDto extends AbstractEntityDto {

    private final String name;

    /**
     * Constructs a new instance of {@link SpecEntityDto}
     *
     * @param entity the {@link SpecEntity} instance to convert to a DTO
     */
    protected SpecEntityDto(SpecEntity entity) {
        super(entity);
        this.name = entity.getName();
    }
}