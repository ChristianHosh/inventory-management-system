package com.chris.ims.item;

import com.chris.ims.entity.RequireAll;
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
@RequestMapping("/items")
class ItemController {

  private final ItemService service;

  @GetMapping("")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful")
  })
  public Page<ItemDto> getItems(
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "25") int size,
          @RequestParam(value = "query", defaultValue = "") String query
  ) {
    return service.getItems(page, size, query);
  }

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  @Validated(RequireAll.class)
  @Operation(responses = {
          @ApiResponse(responseCode = "201", description = "successful creation"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
          @ApiResponse(responseCode = "404", description = "baseUnitId unit not found")
  })
  public ItemDto createItem(@RequestBody @Valid ItemRequest request) {
    return service.createItem(request);
  }

  @GetMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "404", description = "item not found")
  })
  public ItemDto getItem(@PathVariable Long id) {
    return service.getItem(id);
  }

  @PutMapping("/{id}")
  @Validated(RequireAll.class)
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
          @ApiResponse(responseCode = "404", description = "item not found"),
          @ApiResponse(responseCode = "404", description = "baseUnitId unit not found")
  })
  public ItemDto updateItem(@PathVariable Long id, @RequestBody @Valid ItemRequest request) {
    return service.updateItem(id, request);
  }

  @PatchMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
          @ApiResponse(responseCode = "404", description = "item not found"),
          @ApiResponse(responseCode = "404", description = "baseUnitId unit not found")
  })
  public ItemDto patchItem(@PathVariable Long id, @RequestBody @Valid ItemRequest request) {
    return service.patchItem(id, request);
  }

  @DeleteMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "400", description = "item is used"),
          @ApiResponse(responseCode = "404", description = "item not found")
  })
  public ItemDto deleteItem(@PathVariable Long id) {
    return service.deleteItem(id);
  }
}

