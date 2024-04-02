<h1>Introducing StockStream: An Inventory Management System</h1>
<h2>This project was built for COMP4382 (Web Services) course in Birzeit University</h2>
<p>This project implements a warehouse management system to streamline inventory control, customer interactions, and sales tracking. It allows warehouses to:</p>
<ul>
  <li>Manage items with details like name, price, and unit.</li>
  <li>Create and track invoices with customers salespeople, and warehouse refereneces.</li>
  <li>Manage customer and employee contact information</li>
  <li>Specify warehouse settings, including allowing or disallowing negative stock</li>
</ul>
<br/>
<hr/>
<div>
  <h3>API Documentation available on Google Sheets</h3>
  <a href="https://docs.google.com/spreadsheets/d/1ZpMwW9f5QEL47GuOURc2nhgphvwFFmVGspfF8CfqkVE/edit?usp=sharing">Click here to view API documentation</a>
  <h3>API Documentation available on Swagger Editor</h3>
  <a href="api-docs.yaml">Click here to view API documentation</a>
</div>
<br/>
<hr/>
<div>
  <h3>How to use for yourself</h3>
  <ul>
    <li>Install Java 21 <a href="https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html">here</a></li>
    <li>Clone this repository using the following link https://github.com/ChristianHosh/inventory-management-system.git</li>
    <li>If you're using IntelliJ simply run the application</li>
    <li>If you're not using IntelliJ you do the following:
      <ul>
        <li>Navigate to the Project Directory</li>
        <li>Open your terminal e.g. (cmd)</li>
        <li>run the following command <pre>'./mvn spring-boot:run'</pre></li>
      </ul>
      <li>The application should typically start on a default port (usually 8080).</li>
    </li>
  </ul>
</div>
<br/>
<hr/>
<div>
  <h3>The following diagram shows the Entity Relations Diagram</h3>
  <img src="https://github.com/ChristianHosh/inventory-management-system/assets/104357056/4109e58e-1a4c-4e2c-9e19-c5d863b5045e" alt="IMS-ERD" />
</div>
<br/>
<hr/>
<div>
  <h3>Entities</h3>
  <div>
    <h4>AbstractEntity</h4>
    <p>all entities are extended from this mapped super class, which contains the following shared fields</p>
    <ul>
      <li><code>id</code>: (Long) a unique id generated by the database</li>
      <li><code>createdOn</code>: (LocalDateTime) of entity creation</li>
      <li><code>updatedOn</code>: (LocalDateTIme) of last entity update</li>
      <li><code>keyword</code>: (String) automatically generated by specific fields marked with <code>@Keyword</code> annotation</li>
    </ul>
  </div>
  <hr/>
  <div>
    <h4>SpecEntity</h4>
    <p>some entities also require a name, this mapped super class extends AbstractEntity, and can also be extended to add the following field</p>
    <ul>
      <li><code>name</code>: a (String) that representes the name of the entity, this field is also marked with the <code>@Keyword</code> annotation</li>
    </ul>
  </div>
  <hr/>
  <div>
    <h4>Contact</h4>
    <p>represents a contact entity within the system, likely used to manage customer or employee information, it also directly extends the <a href="#specentity">SpecEntity</a> class</p>
    <p>other than the inherited fields, it also contains the following fields:</p>
    <ul>
      <li><code>type</code>: (Enum) with <code>EMPLOYEE</code> and <code>CUSTOMER</code> values, This field is used to categorize the contact as an employee or a customer</li>
    </ul>
  </div>
  <hr/>
  <div>
    <h4>Unit</h4>
    <p>represents a unit of measurement within the system. It inherits from the <a href="#specentity">SpecEntity</a> class</p>
    <p>it also introduces two new field:</p>
    <ul>
      <li><code>factor</code>: (Double) which represents a conversion factor associated with the unit</li>
      <li><code>belongsTo</code>: <a href="#unit">(Unit)</a> where a unit can belong to another unit, creating a conversion chain</li>
    </ul>
  </div>
  <hr/>
  <div>
    <h4>Item</h4>
    <p>represents an item within the system, used for managing product information. It inherits from the <a href="#specentity">SpecEntity</a> class</p>
    <p>it contains the following field also:</p>
    <ul>
      <li><code>basePrice</code>: (Double) which represents the base price of the item</li>
      <li><code>baseUnit</code>: <a href="#unit">(Unit)</a> This suggests a relationship where an item has a single base unit of measurement (e.g., kilogram, meter, bottle,...)</li>
    </ul>
  </div>
  <hr/>
  <div>
    <h4>Warehouse</h4>
    <p>represents a warehouse within the system, used for managing inventory locations. It inherits from the <a href="#specentity">SpecEntity</a> class</p>
    <p>it also introduces the following fields:</p>
    <ul>
      <li><code>allowNegativeStock</code>: (Boolean) which specifies whether the warehouse allows negative stock levels (meaning items can be listed as having a stock quantity below zero)</li>
      <li><code>itemDetails</code>: a set of <a href="#warehouseitemdetail">(WarehouseItemDetail)</a> which represents the item details of the warehouse like quantity</li>
    </ul>
  </div>
  <hr/>
  <div>
    <h4>WarehouseItemDetail</h4>
    <p>represents the details of an item stored within a specific warehouse. It inherits from the <a href="#abstractentity">AbstractEntity</a> class</p>
    <p>it defines the following fields:</p>
    <ul>
      <li><code>item</code>: <a href="#item">(Item)</a> This represents the item associated with this detail</li>
      <li><code>quantity</code>: (Double) which represents the quantity of the item stored in this specific warehouse</li>
    </ul>
  </div>
  <div>
    <h4>Invoice</h4>
    <p>represents an invoice within the system, likely used for managing sales transactions. It inherits from the <a href="#abstractentity">AbstractEntity</a> class</p>
    <p>which has the following fields</p>
    <ul>
      <li><code>customer</code>: <a href="#contact">(Contact)</a> which represnts the customer associated with the invoice</li>
      <li><code>salesman</code>: <a href="#contact">(Contact)</a> which represnts the salesman associated with the invoice</li>
      <li><code>warehouse</code>: <a href="#warehouse">(Warehouse)</a> which represnts the warehouse from which the items were sold</li>
      <li><code>itemDetails</code>: a set of <a href="#invoiceitemdetails">(InvoiceItemDetail)</a> which represents the item details of this invocie</li>
      <li><code>total</code>: (Double) represnt the total price of all items within the invoice</li>
      <li><code>status</code>: (Enum) of <code>PENDING</code> <code>POSTED</code> <code>CANCELLED</code> which represnts the current status of the invoice</li>
    </ul>
  </div>
  <div>
    <h4>InvoiceItemDetail</h4>
    <p>represnts a single line item within an invoice, detailing the item sold. It inherits from the <a href="#abstractentity">AbstractEntity</a> class</p>
    <p>contains the following fields:</p>
    <ul>
      <li><code>item</code>: <a href="#item">(Item)</a> references the sold item</li>
      <li><code>item</code>: <a href="#unit">(Unit)</a> references the sold item unit of measurement for the quantity</li>
      <li><code>quantity</code>: (Double) the number of items sold</li>
      <li><code>unitPrice</code>: (Double) the price per unit</li>
      <li><code>totalPrice</code>: (Double) the calculated price of (quantity * unitPrice)</li>
   </ul>
  </div>
</div>
