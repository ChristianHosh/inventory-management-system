package com.chris.ims.contact;

import com.chris.ims.entity.exception.BxException;
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

  public ContactDto createContact(ContactRequest request) {
    if (request.name() == null)
      throw BxException.badRequest(Contact.class, "name is required");
    if (request.code() == null)
      throw BxException.badRequest(Contact.class, "code is required");
    if (request.isEmployee() == null)
      throw BxException.badRequest(Contact.class, "isEmployee is required");

    Contact contact = new Contact()
            .setIsEmployee(request.isEmployee())
            .setCode(request.code())
            .setName(request.name());

    return contactFacade.save(contact).toDto();
  }

  public ContactDto getContact(Long id) {
    return contactFacade.findById(id).toDto();
  }

  public ContactDto updateContact(Long id, ContactRequest request) {
    Contact contact = contactFacade.findById(id)
            .setIsEmployee(request.isEmployee())
            .setCode(request.code())
            .setName(request.name());

    return contactFacade.save(contact).toDto();
  }

  public ContactDto patchContact(Long id, ContactRequest request) {
    Contact contact = contactFacade.findById(id);

    if (request.code() != null)
      contact.setCode(request.code());
    if (request.name() != null)
      contact.setName(request.name());
    if (request.isEmployee() != null)
      contact.setIsEmployee(request.isEmployee());

    return contactFacade.save(contact).toDto();

  }
}
