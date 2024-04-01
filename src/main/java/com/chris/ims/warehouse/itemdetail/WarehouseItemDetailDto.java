package com.chris.ims.warehouse.itemdetail;

import com.chris.ims.entity.AbstractEntityDto;
import com.chris.ims.item.ItemDto;
import lombok.Getter;

/**
 * Response DTO for {@link WarehouseItemDetail}
 */
@Getter
public class WarehouseItemDetailDto extends AbstractEntityDto {
  private final ItemDto item;
  private final Double quantity;

  protected WarehouseItemDetailDto(WarehouseItemDetail entity) {
    super(entity);
    this.item = entity.getItem().toDto();
    this.quantity = entity.getQuantity();
  }
}