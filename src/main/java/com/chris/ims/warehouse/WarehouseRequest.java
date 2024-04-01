package com.chris.ims.warehouse;

import com.chris.ims.entity.RequireAll;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * Request DTO for {@link Warehouse}
 */
public record WarehouseRequest(

        @NotNull(groups = RequireAll.class, message = "name can't be null")
        @NotBlank(groups = RequireAll.class, message = "name can't be blank'")
        String name,

        @NotNull(groups = RequireAll.class, message = "allowNegativeStock can't be null")
        Boolean allowNegativeStock

) implements Serializable {

}