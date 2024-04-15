package com.chris.ims.item;

import com.chris.ims.entity.RequireAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * The ItemController class is a REST controller that provides endpoints for managing items.
 * It is responsible for handling requests to the /items endpoint, including GET, POST, PUT, PATCH, and DELETE requests.
 * The controller uses the ItemService class to perform operations on the items data.
 */
@RestController
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/items")
public class ItemController {

  private final ItemService service;

  /**
   * Returns a page of items that match the given query.
   *
   * @param page  the page number
   * @param size  the number of items per page
   * @param query the search query
   * @return the page of items
   */
  @GetMapping("")
  public Page<ItemDto> getItems(
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "25") int size,
          @RequestParam(value = "query", defaultValue = "") String query) {
    return service.getItems(page, size, query);
  }

  /**
   * Creates a new item and returns it.
   *
   * @param request the item information
   * @return the created item
   */
  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  @Validated(RequireAll.class)
  public ItemDto createItem(@RequestBody @Valid ItemRequest request) {
    return service.createItem(request);
  }

  /**
   * Returns the item with the given ID.
   *
   * @param id the ID of the item
   * @return the item
   */
  @GetMapping("/{id}")
  public ItemDto getItem(@PathVariable Long id) {
    return service.getItem(id);
  }

  /**
   * Updates the item with the given ID and returns it.
   *
   * @param id      the ID of the item
   * @param request the updated item information
   * @return the updated item
   */
  @PutMapping("/{id}")
  @Validated(RequireAll.class)
  public ItemDto updateItem(@PathVariable Long id, @RequestBody @Valid ItemRequest request) {
    return service.updateItem(id, request);
  }

  /**
   * Partially updates the item with the given ID and returns it.
   *
   * @param id      the ID of the item
   * @param request the updated item information
   * @return the updated item
   */
  @PatchMapping("/{id}")
  public ItemDto patchItem(@PathVariable Long id, @RequestBody @Valid ItemRequest request) {
    return service.patchItem(id, request);
  }

  /**
   * Deletes the item with the given ID and returns it.
   *
   * @param id the ID of the item
   * @return the deleted item
   */
  @DeleteMapping("/{id}")
  public ItemDto deleteItem(@PathVariable Long id) {
    return service.deleteItem(id);
  }
}

