package com.chris.ims.contact;

import com.chris.ims.entity.AbstractEntity;
import com.chris.ims.entity.AbstractEntityFacade;
import com.chris.ims.entity.AbstractEntityRepository;
import com.chris.ims.entity.exception.BxException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * This class provides the business logic for the Contact entity.
 */
@Service
@RequiredArgsConstructor
public class ContactFacade implements AbstractEntityFacade<Contact> {

  private final ContactRepository repository;

  @Override
  public AbstractEntityRepository<Contact> getRepository() {
    return repository;
  }

  @Override
  public Class<Contact> getEntityClass() {
    return Contact.class;
  }

  /**
   * Searches for contacts based on a query and a page size.
   *
   * @param page  the page number
   * @param size  the page size
   * @param query the search query
   * @param group the group code
   * @return the page of contacts
   */
  public Page<Contact> searchQuery(int page, int size, String query, String group) {
    PageRequest request = PageRequest.of(page, size);

    if (group.equalsIgnoreCase(AbstractEntity.GROUP_ALL)) {
      return searchQuery(query, request);
    } else if (group.equalsIgnoreCase(Contact.GROUP_CUSTOMER)) {
      return repository.searchQueryIsEmployee(query, request);
    } else if (group.equalsIgnoreCase(Contact.GROUP_EMPLOYEE)) {
      return repository.searchQueryIsCustomer(query, request);
    }

    throw BxException.badRequest(getEntityClass(), "unknown group code", group);
  }
}
