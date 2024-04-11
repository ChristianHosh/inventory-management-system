package com.chris.ims.contact;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ContactService {

  private final ContactFacade contactFacade;

  public Page<ContactDto> getContacts(int page, int size, String query, String group) {
    return contactFacade.searchQuery(page, size, query, group).map(Contact::toDto);
  }

  public ContactDto getContact(Long id) {
    return contactFacade.findById(id).toDto();
  }

  @Transactional
  public ContactDto createContact(ContactRequest request) {
    Contact contact = contactFacade.newEntity(request);
    contact.setType(request.getType());

    return contactFacade.save(contact).toDto();
  }

  @Transactional
  public ContactDto updateContact(Long id, ContactRequest request) {
    Contact contact = contactFacade.findById(id).edit();

    contact.setName(request.getName());
    contact.setType(request.getType());

    return contactFacade.save(contact).toDto();
  }

  @Transactional
  public ContactDto patchContact(Long id, ContactRequest request) {
    Contact contact = contactFacade.findById(id).edit();

    if (request.getName() != null)
      contact.setName(request.getName());
    if (request.getType() != null)
      contact.setType(request.getType());

    return contactFacade.save(contact).toDto();
  }

  public ContactDto deleteContact(Long id) {
    Contact contact = contactFacade.findById(id);
    return contactFacade.delete(contact).toDto();
  }
}
