package com.chris.ims.warehouse.itemdetail;

import com.chris.ims.entity.RequireAll;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

/**
 * Request DTO for {@link WarehouseItemDetail}
 */
public record WarehouseItemDetailRequest(

        @NotNull(groups = RequireAll.class, message = "itemId can't be null")
        @Positive(groups = RequireAll.class, message = "itemId can't be negative or zero")
        Long itemId,

        @NotNull(groups = RequireAll.class, message = "quantity can't be null")
        Double quantity

) implements Serializable {

}