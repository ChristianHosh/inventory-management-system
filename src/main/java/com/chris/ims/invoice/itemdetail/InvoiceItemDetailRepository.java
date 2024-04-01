package com.chris.ims.invoice.itemdetail;

import com.chris.ims.entity.AbstractEntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

interface InvoiceItemDetailRepository extends AbstractEntityRepository<InvoiceItemDetail> {

  @Query("""
    select i from InvoiceItemDetail i
    where i.invoice.id = :id and
    i.keyword ilike concat('%', :query, '%')
  """)
  Page<InvoiceItemDetail> searchByInvoice(Long id, String query, Pageable pageable);
}