package com.chris.ims.warehouse;

import com.chris.ims.entity.AbstractEntityFacade;
import com.chris.ims.entity.AbstractEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WarehouseFacade implements AbstractEntityFacade<Warehouse> {

  private final WarehouseRepository repository;

  @Override
  public AbstractEntityRepository<Warehouse> getRepository() {
    return repository;
  }

  @Override
  public Class<Warehouse> getEntityClass() {
    return Warehouse.class;
  }
}
