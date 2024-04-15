package com.chris.ims.entity.user;

import com.chris.ims.entity.AbstractEntityFacade;
import com.chris.ims.entity.AbstractEntityRepository;
import com.chris.ims.entity.exception.CxException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade implements AbstractEntityFacade<User> {

  private final UserRepository repository;

  @Override
  public <S extends User> User delete(S entity) {
    if (entity.getString(User.F_USERNAME).equalsIgnoreCase("ADMIN"))
      throw CxException.badRequest(getEntityClass(), "can't delete admin user");

    return AbstractEntityFacade.super.delete(entity);
  }

  @Override
  public AbstractEntityRepository<User> getRepository() {
    return repository;
  }

  @Override
  public Class<User> getEntityClass() {
    return User.class;
  }

  public boolean existsByUsername(String username) {
    return repository.existsByUsername(username);
  }

  public User findByUsername(String username) {
    return repository.findByUsername(username)
            .orElseThrow(CxException.xNotFound(getEntityClass(), username));
  }
}
