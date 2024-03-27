package com.chris.ims.invoice;

import com.chris.ims.contact.Contact;
import com.chris.ims.entity.Keyword;
import com.chris.ims.entity.SpecEntity;
import com.chris.ims.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "t_invoice")
public class Invoice extends SpecEntity {

  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "customer_id", nullable = false)
  private Contact customer;

  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "salesman_id", nullable = false)
  private Contact salesman;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
  @JoinColumn(name = "warehouse_id", nullable = false)
  private Warehouse warehouse;

  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<InvoiceItemDetail> invoiceItemDetails = new LinkedHashSet<>();

  public void setCustomer(Contact customer) {
    if (Boolean.TRUE.equals(customer.getIsEmployee()))
      throw new IllegalArgumentException("Contact must be a customer");
    this.customer = customer;
  }

  public void setSalesman(Contact salesman) {
    if (Boolean.FALSE.equals(salesman.getIsEmployee()))
      throw new IllegalArgumentException("Contact must be an employee");
    this.salesman = salesman;
  }
}