package com.chris.ims.entity.user;

import com.chris.ims.entity.AbstractEntityFacade;
import com.chris.ims.entity.AbstractEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade implements AbstractEntityFacade<User> {
  
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
