package com.chris.ims.contact;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * The ContactService class provides business logic for managing Contacts.
 */
@Service
@RequiredArgsConstructor
public class ContactService {

  private final ContactFacade contactFacade;

  /**
   * Returns a page of Contacts that match the specified search criteria.
   *
   * @param page the page number
   * @param size the page size
   * @param query the search query
   * @param group the group name
   * @return a page of Contacts
   */
  public Page<ContactDto> getContacts(int page, int size, String query, String group) {
    return contactFacade.searchQuery(page, size, query, group).map(Contact::toDto);
  }

  /**
   * Returns the Contact with the specified ID.
   *
   * @param id the ID of the Contact to retrieve
   * @return the Contact
   */
  public ContactDto getContact(Long id) {
    return contactFacade.findById(id).toDto();
  }

  /**
   * Creates a new Contact using the specified request data.
   *
   * @param request the request data
   * @return the newly created Contact
   */
  @Transactional
  public ContactDto createContact(ContactRequest request) {
    Contact contact = new Contact()
           .setType(request.type())
           .setName(request.name());

    return contactFacade.save(contact).toDto();
  }

  /**
   * Updates the Contact with the specified ID using the specified request data.
   *
   * @param id the ID of the Contact to update
   * @param request the request data
   * @return the updated Contact
   */
  @Transactional
  public ContactDto updateContact(Long id, ContactRequest request) {
    Contact contact = contactFacade.findById(id)
           .setType(request.type())
           .setName(request.name());

    return contactFacade.save(contact).toDto();
  }

  /**
   * Partially updates the Contact with the specified ID using the specified request data.
   *
   * @param id the ID of the Contact to update
   * @param request the request data
   * @return the updated Contact
   */
  @Transactional
  public ContactDto patchContact(Long id, ContactRequest request) {
    Contact contact = contactFacade.findById(id);

    if (request.name()!= null)
      contact.setName(request.name());
    if (request.type()!= null)
      contact.setType(request.type());

    return contactFacade.save(contact).toDto();

  }

  /**
   * Deletes the Contact with the specified ID.
   *
   * @param id the ID of the Contact to delete
   * @return the deleted Contact
   */
  public ContactDto deleteContact(Long id) {
    Contact contact = contactFacade.findById(id);
    return contactFacade.delete(contact).toDto();
  }
}
