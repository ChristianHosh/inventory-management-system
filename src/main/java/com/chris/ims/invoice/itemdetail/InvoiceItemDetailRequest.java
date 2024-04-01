package com.chris.ims.invoice.itemdetail;

import com.chris.ims.entity.RequireAll;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;

/**
 * Request DTO for {@link InvoiceItemDetail}
 */
public record InvoiceItemDetailRequest(

        @NotNull(groups = RequireAll.class, message = "itemId can't be null")
        @Positive(groups = RequireAll.class, message = "itemId can't be negative or zero")
        Long itemId,

        @NotNull(groups = RequireAll.class, message = "unitId can't be null")
        @Positive(groups = RequireAll.class, message = "unitId can't be negative or zero")
        Long unitId,

        @NotNull(groups = RequireAll.class, message = "quantity can't be null")
        @PositiveOrZero(groups = RequireAll.class, message = "quantity can't be negative")
        Double quantity,

        Double unitPrice

) implements Serializable {

}