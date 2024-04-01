package com.chris.ims.invoice;

import com.chris.ims.contact.Contact;
import com.chris.ims.contact.ContactType;
import com.chris.ims.entity.AbstractEntity;
import com.chris.ims.entity.Keyword;
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

@Getter
@Setter
@Entity
@Accessors(chain = true)
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

  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<InvoiceItemDetail> itemDetails = new LinkedHashSet<>();


  public Invoice setCustomer(Contact customer) {
    if (!Objects.equals(customer.getType(), ContactType.CUSTOMER))
      throw BxException.badRequest(getClass(), "type", "must be CUSTOMER");
    this.customer = customer;
    return this;
  }

  public Invoice setSalesman(Contact salesman) {
    if (!Objects.equals(salesman.getType(), ContactType.EMPLOYEE))
      throw BxException.badRequest(getClass(), "type", "must be EMPLOYEE");
    this.salesman = salesman;
    return this;
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