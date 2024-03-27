package com.chris.ims.contact;

import com.chris.ims.entity.AbstractEntityRepository;
import com.chris.ims.entity.SpecEntityFacade;
import com.chris.ims.entity.exception.BxException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactFacade implements SpecEntityFacade<Contact> {

  private final ContactRepository repository;

  @Override
  public AbstractEntityRepository<Contact> getRepository() {
    return repository;
  }

  @Override
  public Class<Contact> getEntityClass() {
    return Contact.class;
  }

  public Page<Contact> searchQuery(int page, int size, String query, String group) {
    PageRequest request = PageRequest.of(page, size);

    if (group.equalsIgnoreCase(Contact.GROUP_ALL))
      return searchQuery(query, request);
    if (group.equalsIgnoreCase(Contact.GROUP_CUSTOMER))
      return repository.searchQueryIsEmployee(query, request);
    if (group.equalsIgnoreCase(Contact.GROUP_EMPLOYEE))
      return repository.searchQueryIsCustomer(query, request);

    throw BxException.badRequest(getEntityClass(), "unknown group code", group);
  }
}
