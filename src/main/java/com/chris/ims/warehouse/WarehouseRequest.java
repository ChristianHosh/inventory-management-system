package com.chris.ims.warehouse;

import com.chris.ims.entity.RequireAll;
import com.chris.ims.entity.SpecRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for {@link Warehouse}
 */
@Getter
@Setter(AccessLevel.NONE)
public class WarehouseRequest extends SpecRequest {

  @NotNull(groups = RequireAll.class, message = "allowNegativeStock can't be null")
  private Boolean allowNegativeStock;

}
