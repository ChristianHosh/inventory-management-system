package com.chris.ims.invoice.itemdetail;

import com.chris.ims.entity.SubEntity;
import com.chris.ims.entity.annotations.Keyword;
import com.chris.ims.entity.annotations.Parent;
import com.chris.ims.entity.exception.BxException;
import com.chris.ims.invoice.Invoice;
import com.chris.ims.item.Item;
import com.chris.ims.unit.Unit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "t_invoice_item_detail")
public class InvoiceItemDetail extends SubEntity {

  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "unit_id")
  private Unit unit;

  @Column(name = "quantity", nullable = false)
  private Double quantity;

  @Column(name = "unit_price", nullable = false)
  private Double unitPrice;

  @Parent
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "invoice_id")
  private Invoice invoice;

  @Override
  protected void validate() {
    super.validate();
    
    Unit baseUnit = item.getBaseUnit();
    boolean isValidUnit = false;
    Unit currentUnit = unit;
    while (currentUnit != null) {
      if (Objects.equals(currentUnit, baseUnit)) {
        isValidUnit = true;
        break;
      }
      currentUnit = currentUnit.getBelongsTo();
    }
    
    if (!isValidUnit)
      throw BxException.badRequest(getClass(), "unit", "unit is not valid");
  }

  public void setItem(Item item) {
    this.item = item;
    demandUnitPrice();
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
    demandUnitPrice();
  }

  private void demandUnitPrice() {
    if (unit != null && item != null)
      unitPrice = unit.getTotalFactor() * item.getBasePrice();
  }

  public Double getTotalPrice() {
    return quantity * unitPrice;
  }

  public InvoiceItemDetailDto toDto() {
    return new InvoiceItemDetailDto(this);
  }

}