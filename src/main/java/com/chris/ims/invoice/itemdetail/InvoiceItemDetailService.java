package com.chris.ims.invoice.itemdetail;

import com.chris.ims.item.ItemFacade;
import com.chris.ims.unit.UnitFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceItemDetailService {

  private final InvoiceItemDetailFacade invoiceItemDetailFacade;
  private final ItemFacade itemFacade;
  private final UnitFacade unitFacade;

  public InvoiceItemDetailDto getInvoiceItemDetail(Long id) {
    return invoiceItemDetailFacade.findById(id).toDto();
  }

  public InvoiceItemDetailDto updateInvoiceItemDetail(Long id, InvoiceItemDetailRequest request) {
    InvoiceItemDetail itemDetail = invoiceItemDetailFacade.findById(id);
    itemDetail.getInvoice().checkEditMode();

    itemDetail = itemDetail.setItem(itemFacade.findById(request.itemId()))
            .setUnit(unitFacade.findById(request.unitId()))
            .setQuantity(request.quantity());

    if (request.unitPrice() != null)
      itemDetail.setUnitPrice(request.unitPrice());

    return invoiceItemDetailFacade.save(itemDetail).toDto();
  }

  public InvoiceItemDetailDto patchInvoiceItemDetail(Long id, InvoiceItemDetailRequest request) {
    InvoiceItemDetail itemDetail = invoiceItemDetailFacade.findById(id);
    itemDetail.getInvoice().checkEditMode();

    if (request.itemId() != null)
      itemDetail.setItem(itemFacade.findById(request.itemId()));
    if (request.unitId() != null)
      itemDetail.setUnit(unitFacade.findById(request.unitId()));
    if (request.quantity() != null)
      itemDetail.setQuantity(request.quantity());

    return invoiceItemDetailFacade.save(itemDetail).toDto();
  }

  public InvoiceItemDetailDto deleteInvoiceItemDetail(Long id) {
    InvoiceItemDetail itemDetail = invoiceItemDetailFacade.findById(id);
    itemDetail.getInvoice().checkEditMode();

    return invoiceItemDetailFacade.delete(itemDetail).toDto();
  }
}
