package com.chris.ims.contact;

import com.chris.ims.entity.SpecEntityDto;
import lombok.Getter;

/**
 * Response DTO for {@link Contact}
 */
@Getter
public class ContactDto extends SpecEntityDto {

    private final ContactType type;

    /**
     * Constructs a new {@link ContactDto} instance.
     *
     * @param entity the {@link Contact} entity
     */
    public ContactDto(Contact entity) {
        super(entity);
        this.type = entity.getType();
    }
}
