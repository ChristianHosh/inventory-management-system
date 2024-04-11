package com.chris.ims.item;

import com.chris.ims.entity.RequireAll;
import com.chris.ims.entity.SpecRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for {@link Item}
 */
@Getter
@Setter(AccessLevel.NONE)
public class ItemRequest extends SpecRequest {

  @NotNull(groups = RequireAll.class, message = "baseUnitId can't be null")
  @PositiveOrZero(groups = RequireAll.class, message = "baseUnitId can't be negative or zero")
  private Long baseUnitId;

  @NotNull(groups = RequireAll.class, message = "basePrice can't be null")
  @PositiveOrZero(groups = RequireAll.class, message = "basePrice can't be negative or zero")
  private Double basePrice;

}
