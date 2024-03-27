package com.chris.ims.contact;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/contacts")
class ContactController {

  private final ContactService service;

  @GetMapping("/")
  public Page<ContactDto> getContacts(
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "25") int size,
          @RequestParam(value = "query") String query,
          @RequestParam(value = "group", defaultValue = Contact.GROUP_ALL) String group
  ) {
    return service.getContacts(page, size, query, group);
  }

  @PostMapping("/")
  @ResponseStatus(HttpStatus.CREATED)
  public ContactDto createContact(@RequestBody @Valid ContactRequest request) {
    return service.createContact(request);
  }

  @GetMapping("/{id}")
  public ContactDto getContact(@PathVariable Long id) {
    return service.getContact(id);
  }

  @PutMapping("/{id}")
  public ContactDto updateContact(@PathVariable Long id, @RequestBody @Valid ContactRequest request) {
    return service.updateContact(id, request);
  }

  @PatchMapping("/{id}")
  public ContactDto patchContact(@PathVariable Long id, @RequestBody @Valid ContactRequest request) {
    return service.patchContact(id, request);
  }
}
