package com.chris.ims.invoice;

import com.chris.ims.contact.Contact;
import com.chris.ims.contact.ContactType;
import com.chris.ims.entity.AbstractEntity;
import com.chris.ims.entity.annotations.Keyword;
import com.chris.ims.entity.annotations.SubEntity;
import com.chris.ims.entity.exception.BxException;
import com.chris.ims.invoice.itemdetail.InvoiceItemDetail;
import com.chris.ims.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entity class for invoices
 */
@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "t_invoice")
public class Invoice extends AbstractEntity {

  /**
   * The customer that the invoice is for
   */
  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "customer_id", nullable = false)
  private Contact customer;

  /**
   * The salesman that created the invoice
   */
  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "salesman_id", nullable = false)
  private Contact salesman;

  /**
   * The warehouse that the items are being shipped from
   */
  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
  @JoinColumn(name = "warehouse_id", nullable = false)
  private Warehouse warehouse;

  /**
   * The status of the invoice
   */
  @Keyword
  @Enumerated
  @Column(name = "status", nullable = false)
  private InvoiceStatus status = InvoiceStatus.PENDING;

  /**
   * The details of the items on the invoice
   */
  @SubEntity
  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<InvoiceItemDetail> itemDetails = new LinkedHashSet<>();

  /**
   * Validates the invoice, throwing exceptions if any validation errors are found
   */
  @Override
  protected void validate() {
    super.validate();
    Invoice original = loadOriginal();

    if (status == InvoiceStatus.POSTED && original != null && original.status == InvoiceStatus.PENDING) {
      if (Boolean.FALSE.equals(warehouse.getAllowNegativeStock())) {
        warehouse.getItemDetails().forEach(warehouseItemDetail -> itemDetails.forEach(invoiceItemDetail -> {
          if (Objects.equals(warehouseItemDetail.getItem(), invoiceItemDetail.getItem())) {
            Double invoiceUnitFactor = invoiceItemDetail.getUnit().getTotalFactor();

            Double invoiceItemQuantity = invoiceUnitFactor * invoiceItemDetail.getQuantity();

            if (warehouseItemDetail.getQuantity() < invoiceItemQuantity)
              throw BxException.badRequest(getClass(),
                      "item quantity on detail [" + invoiceItemDetail.getId() + "]",
                      "must be less than or equal to stock");
          }
        }));
      }

      if (getTotal() <= 0) {
        throw BxException.badRequest(getClass(), "total", "must be greater than 0");
      }
    }
  }

  /**
   * Saves the invoice, updating the stock levels of the warehouse as needed
   */
  @Override
  protected void preSave() {
    super.preSave();

    Invoice original = loadOriginal();
    if (status == InvoiceStatus.POSTED && original != null && original.status == InvoiceStatus.PENDING) {
      warehouse.getItemDetails().forEach(warehouseItemDetail -> itemDetails.forEach(invoiceItemDetail -> {
        if (Objects.equals(warehouseItemDetail.getItem(), invoiceItemDetail.getItem())) {
          Double invoiceUnitFactor = invoiceItemDetail.getUnit().getTotalFactor();

          Double newQuantity = warehouseItemDetail.getQuantity() - (invoiceUnitFactor * invoiceItemDetail.getQuantity());
          warehouseItemDetail.setQuantity(newQuantity);
        }
      }));
    }
  }

  /**
   * Indicates whether the invoice can be modified
   */
  @Override
  public boolean canModify() {
    return status == InvoiceStatus.PENDING;
  }

  /**
   * The message to display if the invoice cannot be modified
   */
  @Override
  protected String canModifyMessage() {
    return "invoice can't be modified after posting";
  }

  /**
   * Sets the customer for the invoice
   *
   * @param customer the customer
   * @return the invoice
   */
  public Invoice setCustomer(Contact customer) {
    if (!Objects.equals(customer.getType(), ContactType.CUSTOMER))
      throw BxException.badRequest(getClass(), "type", "must be CUSTOMER");
    this.customer = customer;
    return this;
  }

  /**
   * Sets the salesman for the invoice
   *
   * @param salesman the salesman
   * @return the invoice
   */
  public Invoice setSalesman(Contact salesman) {
    if (!Objects.equals(salesman.getType(), ContactType.EMPLOYEE))
      throw BxException.badRequest(getClass(), "type", "must be EMPLOYEE");
    this.salesman = salesman;
    return this;
  }

  /**
   * Converts the invoice to a DTO
   *
   * @return the DTO
   */
  public InvoiceDto toDto() {
    return new InvoiceDto(this);
  }

  /**
   * Indicates whether the invoice is in the PENDING status
   *
   * @return true if the invoice is pending, false otherwise
   */
  public boolean isPending() {
    return Objects.equals(InvoiceStatus.PENDING, this.status);
  }

  /**
   * Calculates the total amount of the invoice
   *
   * @return the total amount
   */
  public Double getTotal() {
    return itemDetails.stream()
            .mapToDouble(InvoiceItemDetail::getTotalPrice)
            .filter(Double::isFinite)
            .sum();
  }

}