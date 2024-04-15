package com.chris.ims.contact;

import com.chris.ims.entity.AbstractEntity;
import com.chris.ims.entity.RequireAll;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link Contact} entities.
 */
@RestController
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/contacts")
public class ContactController {

  private final ContactService service;

  /**
   * Returns a page of {@link ContactDto} entities that match the given query and group.
   *
   * @param page  the zero-based index of the first result to return
   * @param size  the maximum number of results to return
   * @param query the query string to search for
   * @param group the group to filter by
   * @return a page of {@link ContactDto} entities
   */
  @GetMapping("")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "400", description = "group query parameter is invalid")
  })
  public Page<ContactDto> getContacts(
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "25") int size,
          @RequestParam(value = "query", defaultValue = "") String query,
          @RequestParam(value = "group", defaultValue = AbstractEntity.GROUP_ALL) String group
  ) {
    return service.getContacts(page, size, query, group);
  }

  /**
   * Creates a new {@link Contact} entity based on the given request.
   *
   * @param request the request containing the contact information
   * @return the created {@link ContactDto} entity
   */
  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  @Validated(RequireAll.class)
  @Operation(responses = {
          @ApiResponse(responseCode = "201", description = "successful creation"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid")
  })
  public ContactDto createContact(@RequestBody @Valid ContactRequest request) {
    return service.createContact(request);
  }

  /**
   * Returns the {@link ContactDto} entity with the given ID.
   *
   * @param id the ID of the contact to retrieve
   * @return the {@link ContactDto} entity
   */
  @GetMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "404", description = "contact not found")
  })
  public ContactDto getContact(@PathVariable Long id) {
    return service.getContact(id);
  }

  /**
   * Updates the {@link Contact} entity with the given ID based on the given request.
   *
   * @param id      the ID of the contact to update
   * @param request the request containing the updated contact information
   * @return the updated {@link ContactDto} entity
   */
  @PutMapping("/{id}")
  @Validated(RequireAll.class)
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
          @ApiResponse(responseCode = "404", description = "contact not found")
  })
  public ContactDto updateContact(@PathVariable Long id, @Valid @RequestBody ContactRequest request) {
    return service.updateContact(id, request);
  }

  /**
   * Partially updates the {@link Contact} entity with the given ID based on the given request.
   *
   * @param id      the ID of the contact to update
   * @param request the request containing the updated contact information
   * @return the updated {@link ContactDto} entity
   */
  @PatchMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
          @ApiResponse(responseCode = "404", description = "contact not found")
  })
  public ContactDto patchContact(@PathVariable Long id, @Valid @RequestBody ContactRequest request) {
    return service.patchContact(id, request);
  }

  /**
   * Deletes the {@link Contact} entity with the given ID.
   *
   * @param id the ID of the contact to delete
   * @return the deleted {@link ContactDto} entity
   */
  @DeleteMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "400", description = "contact is used"),
          @ApiResponse(responseCode = "404", description = "contact not found")
  })
  public ContactDto deleteContact(@PathVariable Long id) {
    return service.deleteContact(id);
  }
}
