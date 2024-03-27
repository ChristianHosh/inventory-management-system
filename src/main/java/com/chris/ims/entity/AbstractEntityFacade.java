package com.chris.ims.entity;

import com.chris.ims.entity.exception.BxException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface AbstractEntityFacade<T extends AbstractEntity> {

  default T findById(Long id) {
    return getRepository().findById(id)
            .orElseThrow(BxException.xNotFound(getEntityClass(), id));
  }

  default <S extends T> T save(S entity) {
    return getRepository().save(entity);
  }

  default void deleteById(Long id) {
    getRepository().deleteById(id);
  }

  default <S extends T> void delete(S entity) {
    getRepository().delete(entity);
  }

  default Page<T> findPage(int page, int size) {
    return findPage(PageRequest.of(page, size));
  }

  default Page<T> findPage(Pageable pageable) {
    return getRepository().findAll(pageable);
  }

  default Page<T> searchQuery(String query, Pageable pageable) {
    return getRepository().searchQuery(query, pageable);
  }

  AbstractEntityRepository<T> getRepository();

  Class<T> getEntityClass();
}
