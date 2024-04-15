package com.chris.ims.item;

import com.chris.ims.entity.SpecEntityDto;
import com.chris.ims.unit.UnitDto;
import lombok.Getter;

/**
 * Response DTO for {@link Item}
 */
@Getter
public class ItemDto extends SpecEntityDto {

  private final Double basePrice;
  private final UnitDto baseUnit;

  /**
   * Constructs a new ItemDto instance
   *
   * @param entity the Item entity
   */
  public ItemDto(Item entity) {
    super(entity);
    this.basePrice = entity.getBasePrice();
    this.baseUnit = entity.getBaseUnit().toDto();
  }

}
