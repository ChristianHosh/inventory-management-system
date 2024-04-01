package com.chris.ims.invoice;

import com.chris.ims.entity.RequireAll;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

/**
 * Request DTO for {@link Invoice}
 */
public record InvoiceRequest(

        @NotNull(groups = RequireAll.class, message = "customerId can't be null")
        @Positive(groups = RequireAll.class, message = "customerId can't be negative or zero")
        Long customerId,

        @NotNull(groups = RequireAll.class, message = "salesmanId can't be null")
        @Positive(groups = RequireAll.class, message = "salesmanId can't be negative or zero")
        Long salesmanId,

        @NotNull(groups = RequireAll.class, message = "warehouseId can't be null")
        @Positive(groups = RequireAll.class, message = "warehouseId can't be negative or zero")
        Long warehouseId

) implements Serializable {

}