package com.chris.ims.invoice;

import com.chris.ims.contact.ContactFacade;
import com.chris.ims.entity.exception.CxException;
import com.chris.ims.invoice.itemdetail.InvoiceItemDetail;
import com.chris.ims.invoice.itemdetail.InvoiceItemDetailDto;
import com.chris.ims.invoice.itemdetail.InvoiceItemDetailFacade;
import com.chris.ims.invoice.itemdetail.InvoiceItemDetailRequest;
import com.chris.ims.item.ItemFacade;
import com.chris.ims.unit.UnitFacade;
import com.chris.ims.warehouse.WarehouseFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class InvoiceService {

  private final InvoiceItemDetailFacade invoiceItemDetailFacade;
  private final WarehouseFacade warehouseFacade;
  private final InvoiceFacade invoiceFacade;
  private final ContactFacade contactFacade;
  private final ItemFacade itemFacade;
  private final UnitFacade unitFacade;

  public Page<InvoiceDto> getInvoices(int page, int size, String query) {
    return invoiceFacade.searchQuery(query, page, size).map(Invoice::toDto);
  }

  public InvoiceDto getInvoice(Long id) {
    return invoiceFacade.findById(id).toDto();
  }

  public InvoiceDto createInvoice(InvoiceRequest request) {
    Invoice invoice = invoiceFacade.newEntity();
    invoice.setCustomer(contactFacade.findById(request.salesmanId()));
    invoice.setSalesman(contactFacade.findById(request.salesmanId()));
    invoice.setWarehouse(warehouseFacade.findById(request.warehouseId()));

    return invoiceFacade.save(invoice).toDto();
  }

  public InvoiceDto updateInvoice(Long id, InvoiceRequest request) {
    Invoice invoice = invoiceFacade.findById(id).edit();

    invoice.setCustomer(contactFacade.findById(request.salesmanId()));
    invoice.setSalesman(contactFacade.findById(request.salesmanId()));
    invoice.setWarehouse(warehouseFacade.findById(request.warehouseId()));

    return invoiceFacade.save(invoice).toDto();
  }

  public InvoiceDto patchInvoice(Long id, InvoiceRequest request) {
    Invoice invoice = invoiceFacade.findById(id);

    if (request.customerId() != null)
      invoice.setCustomer(contactFacade.findById(request.customerId()));
    if (request.salesmanId() != null)
      invoice.setSalesman(contactFacade.findById(request.salesmanId()));
    if (request.warehouseId() != null)
      invoice.setWarehouse(warehouseFacade.findById(request.warehouseId()));

    return invoiceFacade.save(invoice).toDto();
  }

  public InvoiceDto deleteInvoice(Long id) {
    Invoice invoice = invoiceFacade.findById(id);

    return invoiceFacade.delete(invoice).toDto();
  }

  public InvoiceItemDetailDto createInvoiceItemDetail(Long id, InvoiceItemDetailRequest request) {
    Invoice invoice = invoiceFacade.findById(id);

    InvoiceItemDetail itemDetail = invoiceItemDetailFacade.newEntity();
    itemDetail.setItem(itemFacade.findById(request.itemId()));
    itemDetail.setUnit(unitFacade.findById(request.unitId()));
    itemDetail.setQuantity(request.quantity());
    itemDetail.setInvoice(invoice);

    if (request.unitPrice() != null)
      itemDetail.setUnitPrice(request.unitPrice());

    return invoiceItemDetailFacade.save(itemDetail).toDto();
  }

  public Page<InvoiceItemDetailDto> getInvoiceDetails(Long id, int page, int size, String query) {
    return invoiceItemDetailFacade.searchQueryByInvoice(id, page, size, query).map(InvoiceItemDetail::toDto);
  }

  public InvoiceDto postInvoice(Long id) {
    Invoice invoice = invoiceFacade.findById(id);

    if (!invoice.isPending())
      throw CxException.badRequest(Invoice.class, "invoice must be pending to post");

    invoice.setStatus(InvoiceStatus.POSTED);
    return invoiceFacade.save(invoice).toDto();
  }

  public InvoiceDto cancelInvoice(Long id) {
    Invoice invoice = invoiceFacade.findById(id);

    if (invoice.isPending())
      throw CxException.badRequest(Invoice.class, "invoice must be posted to cancel");

    invoice.setStatus(InvoiceStatus.CANCELED);
    return invoiceFacade.save(invoice).toDto();
  }
}
