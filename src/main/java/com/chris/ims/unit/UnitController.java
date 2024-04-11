package com.chris.ims.unit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/api/units")
class UnitController {

  private final UnitService service;

  @GetMapping("")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful")
  })
  public Page<UnitDto> getUnits(
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "25") int size,
          @RequestParam(value = "query", defaultValue = "") String query
  ) {
    return service.getUnits(page, size, query);
  }

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(responses = {
          @ApiResponse(responseCode = "201", description = "successful creation"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
          @ApiResponse(responseCode = "404", description = "belongsToId unit not found")
  })
  public UnitDto createUnit(@RequestBody @Valid UnitRequest request) {
    return service.createUnit(request);
  }

  @GetMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "404", description = "unit not found")
  })
  public UnitDto getUnit(@PathVariable Long id) {
    return service.getUnit(id);
  }

  @PutMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
          @ApiResponse(responseCode = "404", description = "unit not found"),
          @ApiResponse(responseCode = "404", description = "belongsToId unit not found")
  })
  public UnitDto updateUnit(@PathVariable Long id, @RequestBody @Valid UnitRequest request) {
    return service.updateUnit(id, request);
  }

  @PatchMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
          @ApiResponse(responseCode = "404", description = "unit not found"),
          @ApiResponse(responseCode = "404", description = "belongsToId unit not found")
  })
  public UnitDto patchUnit(@PathVariable Long id, @RequestBody @Valid UnitRequest request) {
    return service.patchUnit(id, request);
  }

  @DeleteMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "400", description = "unit is used"),
          @ApiResponse(responseCode = "404", description = "unit not found")
  })
  public UnitDto deleteUnit(@PathVariable Long id) {
    return service.deleteUnit(id);
  }
}
