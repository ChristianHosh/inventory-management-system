package com.chris.ims.warehouse;

import com.chris.ims.entity.RequireAll;
import com.chris.ims.warehouse.itemdetail.WarehouseItemDetailDto;
import com.chris.ims.warehouse.itemdetail.WarehouseItemDetailRequest;
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
@RequestMapping("/api/warehouses")
class WarehouseController {

  private final WarehouseService service;

  @GetMapping("")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful")
  })
  public Page<WarehouseDto> getWarehouses(
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "25") int size,
          @RequestParam(value = "query", defaultValue = "") String query
  ) {
    return service.getWarehouses(page, size, query);
  }

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  @Validated(RequireAll.class)
  @Operation(responses = {
          @ApiResponse(responseCode = "201", description = "successful creation"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
  })
  public WarehouseDto createWarehouse(@RequestBody @Valid WarehouseRequest request) {
    return service.createWarehouse(request);
  }

  @GetMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "404", description = "invoice not found")
  })
  public WarehouseDto getWarehouse(@PathVariable Long id) {
    return service.getWarehouse(id);
  }

  @PutMapping("/{id}")
  @Validated(RequireAll.class)
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful update"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
          @ApiResponse(responseCode = "404", description = "warehouse not found")
  })
  public WarehouseDto updateWarehouse(@PathVariable Long id, @RequestBody @Valid WarehouseRequest request) {
    return service.updateWarehouse(id, request);
  }

  @PatchMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful partial update"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
          @ApiResponse(responseCode = "404", description = "warehouse not found")
  })
  public WarehouseDto patchWarehouse(@PathVariable Long id, @RequestBody @Valid WarehouseRequest request) {
    return service.patchWarehouse(id, request);
  }

  @DeleteMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful deletion"),
          @ApiResponse(responseCode = "400", description = "invoice is used"),
  })
  public WarehouseDto deleteWarehouse(@PathVariable Long id) {
    return service.deleteWarehouse(id);
  }

  @GetMapping("/{id}/details")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "404", description = "warehouse not found")
  })
  public Page<WarehouseItemDetailDto> getWarehouseDetails(
          @PathVariable Long id,
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "25") int size,
          @RequestParam(value = "query", defaultValue = "") String query
  ) {
    return service.getWarehouseDetails(id, page, size, query);
  }

  @PostMapping("/{id}/details")
  @ResponseStatus(HttpStatus.CREATED)
  @Validated(RequireAll.class)
  @Operation(responses = {
          @ApiResponse(responseCode = "201", description = "successful creation"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
          @ApiResponse(responseCode = "404", description = "warehouse not found"),
          @ApiResponse(responseCode = "404", description = "itemId Item not found")
  })
  public WarehouseItemDetailDto createWarehouseDetail(@PathVariable Long id, @RequestBody @Valid WarehouseItemDetailRequest request) {
    return service.createWarehouseDetail(id, request);
  }

}
