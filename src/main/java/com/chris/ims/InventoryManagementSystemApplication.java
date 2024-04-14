package com.chris.ims;

import com.chris.ims.contact.Contact;
import com.chris.ims.entity.Project;
import com.chris.ims.entity.user.User;
import com.chris.ims.invoice.Invoice;
import com.chris.ims.invoice.itemdetail.InvoiceItemDetail;
import com.chris.ims.item.Item;
import com.chris.ims.unit.Unit;
import com.chris.ims.warehouse.Warehouse;
import com.chris.ims.warehouse.itemdetail.WarehouseItemDetail;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryManagementSystemApplication {

  public static void main(String[] args) {
    SpringApplication.run(InventoryManagementSystemApplication.class, args);
    Project.createTable(User.class);
    Project.createTable(Contact.class);
    Project.createTable(Invoice.class);
    Project.createTable(InvoiceItemDetail.class);
    Project.createTable(Item.class);
    Project.createTable(Unit.class);
    Project.createTable(Warehouse.class);
    Project.createTable(WarehouseItemDetail.class);
  }

}
