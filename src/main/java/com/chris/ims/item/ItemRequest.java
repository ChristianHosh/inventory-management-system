package com.chris.ims.item;

import com.chris.ims.entity.RequireAll;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;

/**
 * DTO for {@link Item}
 */
public record ItemRequest(

        @NotNull(groups = RequireAll.class, message = "name can't be null")
        @NotBlank(groups = RequireAll.class, message = "name can't be blank'")
        String name,

        @NotNull(groups = RequireAll.class, message = "baseUnitId can't be null")
        @PositiveOrZero(groups = RequireAll.class, message = "baseUnitId can't be negative or zero")
        Long baseUnitId,

        @NotNull(groups = RequireAll.class, message = "basePrice can't be null")
        @PositiveOrZero(groups = RequireAll.class, message = "basePrice can't be negative or zero")
        Double basePrice

) implements Serializable {

}