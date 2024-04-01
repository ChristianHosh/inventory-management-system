package com.chris.ims.invoice;

import com.chris.ims.contact.ContactDto;
import com.chris.ims.entity.AbstractEntityDto;
import com.chris.ims.warehouse.WarehouseDto;
import lombok.Getter;

/**
 * Response DTO for {@link Invoice}
 */
@Getter
public class InvoiceDto extends AbstractEntityDto {

  private final ContactDto customer;
  private final ContactDto salesman;
  private final WarehouseDto warehouse;
  private final InvoiceStatus status;
  private final Double total;

  protected InvoiceDto(Invoice entity) {
    super(entity);
    this.customer = entity.getCustomer().toDto();
    this.salesman = entity.getSalesman().toDto();
    this.warehouse = entity.getWarehouse().toDto();
    this.status = entity.getStatus();
    this.total = entity.getTotal();
  }
}