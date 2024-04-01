package com.chris.ims.invoice;

import com.chris.ims.entity.AbstractEntityFacade;
import com.chris.ims.entity.AbstractEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceFacade implements AbstractEntityFacade<Invoice> {

  private final InvoiceRepository repository;


  @Override
  public AbstractEntityRepository<Invoice> getRepository() {
    return repository;
  }

  @Override
  public Class<Invoice> getEntityClass() {
    return Invoice.class;
  }
}
