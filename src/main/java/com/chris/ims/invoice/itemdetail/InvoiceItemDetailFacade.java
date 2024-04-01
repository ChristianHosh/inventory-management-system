package com.chris.ims.invoice.itemdetail;

import com.chris.ims.entity.AbstractEntityFacade;
import com.chris.ims.entity.AbstractEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceItemDetailFacade implements AbstractEntityFacade<InvoiceItemDetail> {

  private final InvoiceItemDetailRepository repository;

  public Page<InvoiceItemDetail> searchQueryByInvoice(Long id, int page, int size, String query) {
    return repository.searchByInvoice(id, query, PageRequest.of(page, size));
  }

  @Override
  public AbstractEntityRepository<InvoiceItemDetail> getRepository() {
    return repository;
  }

  @Override
  public Class<InvoiceItemDetail> getEntityClass() {
    return InvoiceItemDetail.class;
  }
}
