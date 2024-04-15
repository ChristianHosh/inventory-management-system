package com.chris.ims.invoice;

import com.chris.ims.Resource;
import com.chris.ims.contact.Contact;
import com.chris.ims.contact.ContactType;
import com.chris.ims.entity.AbstractEntity;
import com.chris.ims.entity.BooleanFld;
import com.chris.ims.entity.DbTable;
import com.chris.ims.entity.annotations.Keyword;
import com.chris.ims.entity.annotations.Res;
import com.chris.ims.entity.annotations.SubEntityList;
import com.chris.ims.entity.exception.CxException;
import com.chris.ims.entity.utils.CResources;
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
@Res("invoice")
@Table(name = "t_invoice")
public class Invoice extends AbstractEntity {

  public static final int F_CUSTOMER = CResources.create("customer");
  public static final int F_SALESMAN = CResources.create("salesman");
  public static final int F_WAREHOUSE = CResources.create("warehouse");
  public static final int F_STATUS = CResources.create("status");
  public static final int F_ITEM_DETAILS = Resource.F_INVOICE_ITEM_DETAILS;

  public static void scripts(DbTable table) {
    BooleanFld statusIsPending = table.findField(F_STATUS).expEQ(InvoiceStatus.PENDING);
    table.getFields().values().forEach(
            fld -> fld.setEnabled(statusIsPending)
    );
  }

  @Res("customer")
  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "customer_id", nullable = false)
  private Contact customer;

  @Res("salesman")
  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "salesman_id", nullable = false)
  private Contact salesman;

  @Res("warehouse")
  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
  @JoinColumn(name = "warehouse_id", nullable = false)
  private Warehouse warehouse;

  @Res("status")
  @Keyword
  @Enumerated
  @Column(name = "status", nullable = false)
  private InvoiceStatus status = InvoiceStatus.PENDING;

  @Res("invoiceItemDetail")
  @SubEntityList
  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<InvoiceItemDetail> itemDetails = new LinkedHashSet<>();

  @Override
  protected void validate() {
    super.validate();

    if (!customer.getField(Contact.F_TYPE).equals(ContactType.CUSTOMER))
      throw CxException.badRequest(getClass(), "customer", "type must be CUSTOMER");

    if (!salesman.getField(Contact.F_TYPE).equals(ContactType.EMPLOYEE))
      throw CxException.badRequest(getClass(), "salesman", "type must be EMPLOYEE");

    validateAfterPosting();
  }

  private void validateAfterPosting() {
    Invoice original = loadOriginal();
    if (status == InvoiceStatus.POSTED && original != null && original.status == InvoiceStatus.PENDING) {
      if (Boolean.FALSE.equals(warehouse.getAllowNegativeStock())) {
        warehouse.getItemDetails().forEach(warehouseItemDetail -> itemDetails.forEach(invoiceItemDetail -> {
          if (Objects.equals(warehouseItemDetail.getItem(), invoiceItemDetail.getItem())) {
            Double invoiceUnitFactor = invoiceItemDetail.getUnit().getTotalFactor();

            Double invoiceItemQuantity = invoiceUnitFactor * invoiceItemDetail.getQuantity();

            if (warehouseItemDetail.getQuantity() < invoiceItemQuantity)
              throw CxException.badRequest(getClass(),
                      "item quantity on detail [" + invoiceItemDetail.getId() + "]",
                      "must be less than or equal to stock");
          }
        }));
      }

      if (getTotal() <= 0) {
        throw CxException.badRequest(getClass(), "total", "must be greater than 0");
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