package com.chris.ims.invoice;

import com.chris.ims.entity.RequireAll;
import com.chris.ims.invoice.itemdetail.InvoiceItemDetailDto;
import com.chris.ims.invoice.itemdetail.InvoiceItemDetailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link Invoice}s.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/invoices")
public class InvoiceController {

  private final InvoiceService service;

  /**
   * GET  /invoices : get all the invoices.
   *
   * @param page the page number to retrieve
   * @param size the number of invoices per page
   * @param query the query to search for
   * @return the ResponseEntity with status 200 (OK) and the list of invoices in body
   */
  @GetMapping("")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful")
  })
  public Page<InvoiceDto> getInvoices(
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "25") int size,
          @RequestParam(value = "query", defaultValue = "") String query) {
    return service.getInvoices(page, size, query);
  }

  /**
   * POST  /invoices : create a new invoice.
   *
   * @param request the invoice to create
   * @return the ResponseEntity with status 201 (Created) and with body the new invoice, or with status 400 (Bad Request) if the invoice has already an ID
   */
  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  @Validated(RequireAll.class)
  @Operation(responses = {
          @ApiResponse(responseCode = "201", description = "successful creation"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
          @ApiResponse(responseCode = "404", description = "customerId Contact not found"),
          @ApiResponse(responseCode = "404", description = "salesmanId Contact not found"),
          @ApiResponse(responseCode = "404", description = "warehouseId Warehouse not found")
  })
  public InvoiceDto createInvoice(@Valid @RequestBody InvoiceRequest request) {
    return service.createInvoice(request);
  }

  /**
   * GET  /invoices/{id} : get the "id" invoice.
   *
   * @param id the id of the invoice to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the invoice, or with status 404 (Not Found)
   */
  @GetMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "404", description = "invoice not found")
  })
  public InvoiceDto getInvoice(@PathVariable Long id) {
    return service.getInvoice(id);
  }

  /**
   * PUT  /invoices/{id} : update the "id" invoice.
   *
   * @param id the id of the invoice to update
   * @param request the invoice to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated invoice, or with status 400 (Bad Request) if the invoice is not valid, or with status 500 (Internal Server Error) if the invoice couldnt be updated
   */
  @PutMapping("/{id}")
  @Validated(RequireAll.class)
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful update"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
          @ApiResponse(responseCode = "404", description = "invoice not found"),
          @ApiResponse(responseCode = "404", description = "customerId Contact not found"),
          @ApiResponse(responseCode = "404", description = "salesmanId Contact not found"),
          @ApiResponse(responseCode = "404", description = "warehouseId Warehouse not found")
  })
  public InvoiceDto updateInvoice(@PathVariable Long id, @RequestBody @Valid InvoiceRequest request) {
    return service.updateInvoice(id, request);
  }

  /**
   * PATCH  /invoices/{id} : partially update the "id" invoice.
   *
   * @param id the id of the invoice to update
   * @param request the invoice to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated invoice, or with status 400 (Bad Request) if the invoice is not valid, or with status 500 (Internal Server Error) if the invoice couldnt be updated
   */
  @PatchMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful partial update"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
          @ApiResponse(responseCode = "404", description = "invoice not found"),
          @ApiResponse(responseCode = "404", description = "customerId Contact not found"),
          @ApiResponse(responseCode = "404", description = "salesmanId Contact not found"),
          @ApiResponse(responseCode = "404", description = "warehouseId Warehouse not found")
  })
  public InvoiceDto patchInvoice(@PathVariable Long id, @RequestBody @Valid InvoiceRequest request) {
    return service.patchInvoice(id, request);
  }

  /**
   * DELETE  /invoices/{id} : delete the "id" invoice.
   *
   * @param id the id of the invoice to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful deletion"),
          @ApiResponse(responseCode = "400", description = "invoice is used"),
          @ApiResponse(responseCode = "403", description = "invoice is not pending")
  })
  public InvoiceDto deleteInvoice(@PathVariable Long id) {
    return service.deleteInvoice(id);
  }

  /**
   * PUT  /invoices/{id}/post : post the "id" invoice.
   *
   * @param id the id of the invoice to post
   * @return the ResponseEntity with status 200 (OK) and with body the updated invoice, or with status 400 (Bad Request) if the invoice is not valid, or with status 500 (Internal Server Error) if the invoice couldnt be updated
   */
  @PutMapping("/{id}/post")
  public InvoiceDto postInvoice(@PathVariable Long id) {
    return service.postInvoice(id);
  }

  /**
   * PUT  /invoices/{id}/cancel : cancel the "id" invoice.
   *
   * @param id the id of the invoice to cancel
   * @return the ResponseEntity with status 200 (OK) and with body the updated invoice, or with status 400 (Bad Request) if the invoice is not valid, or with status 500 (Internal Server Error) if the invoice couldnt be updated
   */
  @PutMapping("/{id}/cancel")
  public InvoiceDto cancelInvoice(@PathVariable Long id) {
    return service.cancelInvoice(id);
  }

  /**
   * GET  /invoices/{id}/details : get the invoice details of the "id" invoice.
   *
   * @param id the id of the invoice to retrieve the details for
   * @param page the page number to retrieve
   * @param size the number of invoices per page
   * @param query the query to search for
   * @return the ResponseEntity with status 200 (OK) and the list of invoice details in body
   */
  @GetMapping("/{id}/details")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "404", description = "invoice not found")
  })
  public Page<InvoiceItemDetailDto> getInvoiceDetails(
          @PathVariable Long id,
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "25") int size,
          @RequestParam(value = "query", defaultValue = "") String query) {
    return service.getInvoiceDetails(id, page, size, query);
  }

  /**
   * POST  /invoices/{id}/details : create a new invoice detail for the "id" invoice.
   *
   * @param id the id of the invoice to create the detail for
   * @param request the invoice detail to create
   * @return the ResponseEntity with status 201 (Created) and with body the new invoice detail, or with status 400 (Bad Request) if the invoice detail is not valid, or with status 404 (Not Found) if the invoice or the item or the unit to create the detail for is not found
   */
  @PostMapping("/{id}/details")
  @ResponseStatus(HttpStatus.CREATED)
  @Validated(RequireAll.class)
  @Operation(responses = {
          @ApiResponse(responseCode = "201", description = "successful creation"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
          @ApiResponse(responseCode = "404", description = "invoice not found"),
          @ApiResponse(responseCode = "404", description = "itemId Item not found"),
          @ApiResponse(responseCode = "404", description = "unitId Unit not found")
  })
  public InvoiceItemDetailDto createInvoiceDetail(@PathVariable Long id, @RequestBody @Valid InvoiceItemDetailRequest request) {
   return service.createInvoiceItemDetail(id, request);
  }
}
