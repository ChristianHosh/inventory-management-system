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
    Contact contact = new Contact()
            .setType(request.type())
            .setName(request.name());

    return contactFacade.save(contact).toDto();
  }

  @Transactional
  public ContactDto updateContact(Long id, ContactRequest request) {
    Contact contact = contactFacade.findById(id)
            .setType(request.type())
            .setName(request.name());

    return contactFacade.save(contact).toDto();
  }

  @Transactional
  public ContactDto patchContact(Long id, ContactRequest request) {
    Contact contact = contactFacade.findById(id);

    if (request.name() != null)
      contact.setName(request.name());
    if (request.type() != null)
      contact.setType(request.type());

    return contactFacade.save(contact).toDto();

  }

  public ContactDto deleteContact(Long id) {
    Contact contact = contactFacade.findById(id);
    return contactFacade.delete(contact).toDto();
  }
}
