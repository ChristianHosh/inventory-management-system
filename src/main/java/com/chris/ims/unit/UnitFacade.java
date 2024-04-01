package com.chris.ims.unit;

import com.chris.ims.entity.AbstractEntityFacade;
import com.chris.ims.entity.AbstractEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnitFacade implements AbstractEntityFacade<Unit> {

  private final UnitRepository repository;

  @Override
  public AbstractEntityRepository<Unit> getRepository() {
    return repository;
  }

  @Override
  public Class<Unit> getEntityClass() {
    return Unit.class;
  }
}
