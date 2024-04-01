package com.chris.ims.warehouse;

import com.chris.ims.entity.SpecEntityDto;
import lombok.Getter;

@Getter
public class WarehouseDto extends SpecEntityDto {

  private final Boolean allowNegativeStock;

  protected WarehouseDto(Warehouse entity) {
    super(entity);
    this.allowNegativeStock = entity.getAllowNegativeStock();
  }
}
