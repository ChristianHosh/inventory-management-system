package com.chris.ims.invoice;

import com.chris.ims.contact.Contact;
import com.chris.ims.contact.ContactType;
import com.chris.ims.entity.AbstractEntity;
import com.chris.ims.entity.annotations.Keyword;
import com.chris.ims.entity.annotations.SubEntityList;
import com.chris.ims.entity.exception.BxException;
import com.chris.ims.invoice.itemdetail.InvoiceItemDetail;
import com.chris.ims.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "t_invoice")
public class Invoice extends AbstractEntity {

  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "customer_id", nullable = false)
  private Contact customer;

  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "salesman_id", nullable = false)
  private Contact salesman;

  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
  @JoinColumn(name = "warehouse_id", nullable = false)
  private Warehouse warehouse;

  @Keyword
  @Enumerated
  @Column(name = "status", nullable = false)
  private InvoiceStatus status = InvoiceStatus.PENDING;

  @SubEntityList
  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<InvoiceItemDetail> itemDetails = new LinkedHashSet<>();

  @Override
  protected void validate() {
    super.validate();
    Invoice original = loadOriginal();

    if (!customer.getType().equals(ContactType.CUSTOMER))
      throw BxException.badRequest(getClass(), "customer", "type must be CUSTOMER");

    if (!salesman.getType().equals(ContactType.EMPLOYEE))
      throw BxException.badRequest(getClass(), "salesman", "type must be EMPLOYEE");

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

  @Override
  public boolean canEdit() {
    return status == InvoiceStatus.PENDING;
  }

  @Override
  protected String cantEditMessage() {
    return "invoice can't be modified after posting";
  }

  public InvoiceDto toDto() {
    return new InvoiceDto(this);
  }

  public boolean isPending() {
    return Objects.equals(InvoiceStatus.PENDING, this.status);
  }

  public Double getTotal() {
    return itemDetails.stream()
        .mapToDouble(InvoiceItemDetail::getTotalPrice)
        .filter(Double::isFinite)
        .sum();
  }

}