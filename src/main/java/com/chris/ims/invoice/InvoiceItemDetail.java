package com.chris.ims.invoice;

import com.chris.ims.entity.AbstractEntity;
import com.chris.ims.item.Item;
import com.chris.ims.unit.Unit;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_invoice_item_detail")
public class InvoiceItemDetail extends AbstractEntity {

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "unit_id")
  private Unit unit;

  @Column(name = "quantity", nullable = false)
  private Double quantity;

  @Column(name = "unit_price", nullable = false)
  private Double unitPrice;

  @Column(name = "total_price", nullable = false)
  @Setter(AccessLevel.PRIVATE)
  private Double totalPrice;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "invoice_id", unique = true)
  private Invoice invoice;

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
    demandTotalPrice();
  }

  public void setUnitPrice(Double unitPrice) {
    this.unitPrice = unitPrice;
    demandTotalPrice();
  }

  private void demandTotalPrice() {
    totalPrice = quantity * unitPrice;
  }

  @Override
  protected void preSave() {
    super.preSave();
    demandTotalPrice();
  }
}