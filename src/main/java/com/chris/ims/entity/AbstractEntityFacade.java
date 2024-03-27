package com.chris.ims.entity;

import com.chris.ims.entity.exception.BxException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityFacade<T extends AbstractEntity> {

  default T findById(Long id) {
    return getRepository().findById(id)
            .orElseThrow(BxException.xNotFound(getEntityClass(), id));
  }

  default T save(T entity) {
    return getRepository().save(entity);
  }

  default void deleteById(Long id) {
    getRepository().deleteById(id);
  }

  default void delete(T entity) {
    getRepository().delete(entity);
  }

  default Page<T> findPage(int page, int size) {
    return findPage(PageRequest.of(page, size));
  }

  default Page<T> findPage(Pageable pageable) {
    return getRepository().findAll(pageable);
  }

  JpaRepository<T, Long> getRepository();

  Class<T> getEntityClass();
}
