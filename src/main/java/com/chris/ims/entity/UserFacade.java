package com.chris.ims.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade extends AbstractEntityFacade<User>{
  
  private final UserRepository repository;
  
  @Override
  public AbstractEntityRepository<User> getRepository() {
    return repository;
  }

  @Override
  public Class<User> getEntityClass() {
    return User.class;
  }
}
