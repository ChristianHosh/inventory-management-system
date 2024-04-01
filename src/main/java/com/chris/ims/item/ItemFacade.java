package com.chris.ims.item;

import com.chris.ims.entity.AbstractEntityFacade;
import com.chris.ims.entity.AbstractEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemFacade implements AbstractEntityFacade<Item> {

  private final ItemRepository repository;

  @Override
  public AbstractEntityRepository<Item> getRepository() {
    return repository;
  }

  @Override
  public Class<Item> getEntityClass() {
    return Item.class;
  }
}
