package com.chris.ims.warehouse.itemdetail;

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
@RequestMapping("/api/warehouse-item-details")
public class WarehouseItemDetailController {

  private final WarehouseItemDetailService service;

  @GetMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "404", description = "invoice item detail not found", content = {
                  @Content(schema = @Schema(implementation = ApiError.class))
          })
  })
  public WarehouseItemDetailDto getWarehouseItemDetail(@PathVariable Long id) {
    return service.getWarehouseItemDetail(id);
  }

  @PutMapping("/{id}")
  @Validated(RequireAll.class)
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful update"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid", content = {
                  @Content(schema = @Schema(implementation = ApiError.class))
          }),
          @ApiResponse(responseCode = "404", description = "invoice item detail not found", content = {
                  @Content(schema = @Schema(implementation = ApiError.class))
          })
  })
  public WarehouseItemDetailDto updateWarehouseItemDetail(@PathVariable Long id, @RequestBody @Valid WarehouseItemDetailRequest request) {
    return service.updateWarehouseItemDetail(id, request);
  }

  @PatchMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful partial update"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid", content = {
                  @Content(schema = @Schema(implementation = ApiError.class))
          }),
          @ApiResponse(responseCode = "404", description = "invoice item detail not found", content = {
                  @Content(schema = @Schema(implementation = ApiError.class))
          })
  })
  public WarehouseItemDetailDto patchWarehouseItemDetail(@PathVariable Long id, @RequestBody @Valid WarehouseItemDetailRequest request) {
    return service.patchWarehouseItemDetail(id, request);
  }

  @DeleteMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful deletion"),
          @ApiResponse(responseCode = "400", description = "invoice item detail is used", content = {
                  @Content(schema = @Schema(implementation = ApiError.class))
          }),
          @ApiResponse(responseCode = "404", description = "invoice item detail not found", content = {
                  @Content(schema = @Schema(implementation = ApiError.class))
          })
  })
  public WarehouseItemDetailDto deleteWarehouseItemDetail(@PathVariable Long id) {
    return service.deleteWarehouseItemDetail(id);
  }
}
