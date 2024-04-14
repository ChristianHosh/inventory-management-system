package com.chris.ims;

import com.chris.ims.entity.utils.CResources;
import com.chris.ims.invoice.Invoice;
import com.chris.ims.invoice.itemdetail.InvoiceItemDetail;
import com.chris.ims.item.Item;
import com.chris.ims.unit.Unit;
import com.chris.ims.warehouse.Warehouse;

public interface Resource extends com.chris.ims.entity.utils.Resource {

  int F_QUANTITY = CResources.create("quantity");
  int F_UNIT_PRICE = CResources.create("unitPrice");
  int F_ITEM = CResources.create(Item.class);
  int F_UNIT = CResources.create(Unit.class);
  int F_WAREHOUSE = CResources.create(Warehouse.class);
  int F_INVOICE = CResources.create(Invoice.class);
  int F_INVOICE_ITEM_DETAILS = CResources.create(InvoiceItemDetail.class);
}
