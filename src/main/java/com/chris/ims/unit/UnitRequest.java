package com.chris.ims.unit;

import com.chris.ims.entity.RequireAll;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

/**
 * Request DTO for {@link Unit}
 */
public record UnitRequest(

        @NotNull(groups = RequireAll.class, message = "name can't be null")
        @NotBlank(groups = RequireAll.class, message = "name can't be blank'")
        String name,

        @NotNull(groups = RequireAll.class, message = "factor can't be null")
        @Positive(groups = RequireAll.class, message = "factor can't be negative or zero")
        Double factor,

        Long belongsToId

) implements Serializable {

}