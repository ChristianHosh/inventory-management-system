package com.chris.ims.entity;

import com.chris.ims.entity.exception.BxException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecEntityFacade<T extends SpecEntity> extends AbstractEntityFacade<T> {

  default T findByCode(String code) {
    JpaRepository<T, Long> repository = getRepository();
    if (repository instanceof SpecEntityRepository<T> specRepository) {
      return specRepository.findByCode(code)
              .orElseThrow(BxException.xNotFound(getEntityClass(), code));
    }
    throw new IllegalStateException("facade's repository must be a spec repository");
  }

  default boolean existsByCode(String code) {
    JpaRepository<T, Long> repository = getRepository();
    if (repository instanceof SpecEntityRepository<T> specRepository) {
      return specRepository.existsByCode(code);
    }
    throw new IllegalStateException("facade's repository must be a spec repository");
  }

  @Override
  default <S extends T> T save(S entity) {
    if (existsByCode(entity.getCode())) {
      throw BxException.conflict(getEntityClass(), "code", entity.getCode());
    }
    return AbstractEntityFacade.super.save(entity);
  }
}
