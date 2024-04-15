package com.chris.ims.invoice.itemdetail;

import com.chris.ims.entity.AbstractEntity;
import com.chris.ims.entity.annotations.Keyword;
import com.chris.ims.entity.exception.BxException;
import com.chris.ims.invoice.Invoice;
import com.chris.ims.item.Item;
import com.chris.ims.unit.Unit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * Entity class for invoice item detail
 */
@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "t_invoice_item_detail")
public class InvoiceItemDetail extends AbstractEntity {

  /**
   * The item associated with this invoice item detail
   */
  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  /**
   * The unit associated with this invoice item detail
   */
  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "unit_id")
  private Unit unit;

  /**
   * The quantity of items associated with this invoice item detail
   */
  @Column(name = "quantity", nullable = false)
  private Double quantity;

  /**
   * The unit price of the items associated with this invoice item detail
   */
  @Column(name = "unit_price", nullable = false)
  private Double unitPrice;

  /**
   * The invoice associated with this invoice item detail
   */
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "invoice_id")
  private Invoice invoice;

  /**
   * Validate the fields of this entity
   *
   * @throws BxException if the fields are invalid
   */
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

    if (!isValidUnit) {
      throw BxException.badRequest(getClass(), "unit", "unit is not valid");
    }
  }

  /**
   * Set the item associated with this invoice item detail
   *
   * @param item the item to set
   * @return this instance
   */
  public InvoiceItemDetail setItem(Item item) {
    this.item = item;
    demandUnitPrice();
    return this;
  }

  /**
   * Set the unit associated with this invoice item detail
   *
   * @param unit the unit to set
   * @return this instance
   */
  public InvoiceItemDetail setUnit(Unit unit) {
    this.unit = unit;
    demandUnitPrice();
    return this;
  }

  /**
   * Demand the unit price based on the unit and item fields
   */
  private void demandUnitPrice() {
    if (unit != null && item != null) {
      unitPrice = unit.getTotalFactor() * item.getBasePrice();
    }
  }

  /**
   * Get the total price of the items associated with this invoice item detail
   *
   * @return the total price
   */
  public Double getTotalPrice() {
    return quantity * unitPrice;
  }

  /**
   * Convert this instance to a DTO
   *
   * @return the DTO
   */
  public InvoiceItemDetailDto toDto() {
    return new InvoiceItemDetailDto(this);
  }
}