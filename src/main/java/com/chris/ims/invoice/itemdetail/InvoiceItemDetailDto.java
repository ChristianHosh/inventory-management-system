package com.chris.ims.invoice.itemdetail;

import com.chris.ims.entity.AbstractEntityDto;
import com.chris.ims.item.ItemDto;
import com.chris.ims.unit.UnitDto;
import lombok.Getter;

@Getter
public class InvoiceItemDetailDto extends AbstractEntityDto {

  private final ItemDto item;
  private final UnitDto unit;
  private final Double unitPrice;
  private final Double quantity;
  private final Double totalPrice;

  protected InvoiceItemDetailDto(InvoiceItemDetail entity) {
    super(entity);
    this.item = entity.getItem().toDto();
    this.unit = entity.getUnit().toDto();
    this.unitPrice = entity.getUnitPrice();
    this.quantity = entity.getQuantity();
    this.totalPrice = entity.getTotalPrice();
  }
}
