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

@RestController
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/api/invoices")
public class InvoiceController {

  private final InvoiceService service;

  @GetMapping("")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful")
  })
  public Page<InvoiceDto> getInvoices(
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "25") int size,
          @RequestParam(value = "query", defaultValue = "") String query
  ) {
    return service.getInvoices(page, size, query);
  }

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

  @GetMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "404", description = "invoice not found")
  })
  public InvoiceDto getInvoice(@PathVariable Long id) {
    return service.getInvoice(id);
  }

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

  @DeleteMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful deletion"),
          @ApiResponse(responseCode = "400", description = "invoice is used"),
          @ApiResponse(responseCode = "403", description = "invoice is not pending")
  })
  public InvoiceDto deleteInvoice(@PathVariable Long id) {
    return service.deleteInvoice(id);
  }

  @PutMapping("/{id}/post")
  public InvoiceDto postInvoice(@PathVariable Long id) {
    return service.postInvoice(id);
  }

  @PutMapping("/{id}/cancel")
  public InvoiceDto cancelInvoice(@PathVariable Long id) {
    return service.cancelInvoice(id);
  }

  @GetMapping("/{id}/details")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "404", description = "invoice not found")
  })
  public Page<InvoiceItemDetailDto> getInvoiceDetails(
          @PathVariable Long id,
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "25") int size,
          @RequestParam(value = "query", defaultValue = "") String query
  ) {
    return service.getInvoiceDetails(id, page, size, query);
  }


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
