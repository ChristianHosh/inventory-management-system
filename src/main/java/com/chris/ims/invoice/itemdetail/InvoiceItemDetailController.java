package com.chris.ims.invoice.itemdetail;

import com.chris.ims.entity.RequireAll;
import com.chris.ims.entity.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/api/invoice-item-details")
public class InvoiceItemDetailController {

  private final InvoiceItemDetailService service;

  @GetMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful")
  })
  public InvoiceItemDetailDto getInvoiceItemDetail(@PathVariable Long id) {
    return service.getInvoiceItemDetail(id);
  }

  @PutMapping("/{id}")
  @Validated(RequireAll.class)
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful update"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid", content = {
                  @Content(schema = @Schema(implementation = ApiError.class))
          }),
          @ApiResponse(responseCode = "404", description = "invoiceItemDetail not found")
  })
  public InvoiceItemDetailDto updateInvoiceItemDetail(@PathVariable Long id, @RequestBody @Valid InvoiceItemDetailRequest request) {
    return service.updateInvoiceItemDetail(id, request);
  }

  @PatchMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful update"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid", content = {
                  @Content(schema = @Schema(implementation = ApiError.class))
          }),
          @ApiResponse(responseCode = "404", description = "invoice item detail not found", content = {
                  @Content(schema = @Schema(implementation = ApiError.class))
          }),
  })
  public InvoiceItemDetailDto patchInvoiceItemDetail(@PathVariable Long id, @RequestBody @Valid InvoiceItemDetailRequest request) {
    return service.patchInvoiceItemDetail(id, request);
  }

  @DeleteMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful deletion"),
          @ApiResponse(responseCode = "400", description = "invoice item detail is used", content = {
                  @Content(schema = @Schema(implementation = ApiError.class))
          }),
          @ApiResponse(responseCode = "403", description = "invoice is posted", content = {
                  @Content(schema = @Schema(implementation = ApiError.class))
          }),
          @ApiResponse(responseCode = "404", description = "invoice item detail not found")
  })
  public InvoiceItemDetailDto deleteInvoiceItemDetail(@PathVariable Long id) {
    return service.deleteInvoiceItemDetail(id);
  }
}
