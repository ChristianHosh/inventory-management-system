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

@RestController
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/contacts")
class ContactController {

  private final ContactService service;

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

  @GetMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "404", description = "contact not found")
  })
  public ContactDto getContact(@PathVariable Long id) {
    return service.getContact(id);
  }

  @PutMapping("/{id}")
  @Validated(RequireAll.class)
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
          @ApiResponse(responseCode = "404", description = "contact not found")
  })
  public ContactDto updateContact(@PathVariable Long id, @RequestBody @Valid ContactRequest request) {
    return service.updateContact(id, request);
  }

  @PatchMapping("/{id}")
  @Operation(responses = {
          @ApiResponse(responseCode = "200", description = "successful"),
          @ApiResponse(responseCode = "400", description = "request parameters are invalid"),
          @ApiResponse(responseCode = "404", description = "contact not found")
  })
  public ContactDto patchContact(@PathVariable Long id, @RequestBody @Valid ContactRequest request) {
    return service.patchContact(id, request);
  }

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
