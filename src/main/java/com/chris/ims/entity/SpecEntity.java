package com.chris.ims.entity;

import com.chris.ims.entity.annotations.Keyword;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * The SpecEntity class is the base class for all entities that have a name and a description.
 * It provides basic functionality for managing the creation and modification dates of the entity.
 * The name is stored in the database as keywords for fast searching.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class SpecEntity extends AbstractEntity {

    /**
     * The name of the entity.
     */
    @Keyword
    @Column(name = "name")
    private String name;

    /**
     * Sets the name of the entity.
     *
     * @param name The name to set
     * @return The current instance of the SpecEntity class
     */
    @SuppressWarnings("unchecked")
    public <T extends SpecEntity> T setName(String name) {
        this.name = name;
        return (T) this;
    }

}