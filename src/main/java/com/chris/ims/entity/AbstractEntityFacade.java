package com.chris.ims.entity;

import com.chris.ims.entity.exception.BxException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface AbstractEntityFacade<T extends AbstractEntity> {

  default T newEntity() {
    try {
      T entity = getEntityClass().getConstructor().newInstance();
      entity.setMode(Mode.NEW);
      return entity;
    } catch (Exception e) {
      return null;
    }
  }

  default T newEntity(SpecRequest request) {
    T entity = newEntity();
    if (entity instanceof SpecEntity spec)
      spec.setName(request.getName());
    return entity;
  }

  default T findById(Long id) {
    return getRepository().findById(id)
            .orElseThrow(BxException.xNotFound(getEntityClass(), id));
  }

  default <S extends T> T save(S entity) {
    if (entity instanceof SubEntity subEntity) {
      subEntity.getParent().edit();
    }

    if (!entity.isEditing()) {
      throw new IllegalStateException("entity: " + entity + " is not in edit mode");
    }

    entity.validate();
    entity = getRepository().save(entity);
    entity.setMode(Mode.NORMAL);
    return entity;
  }

  default <S extends T> T delete(S entity) {
    if (entity instanceof SubEntity subEntity) {
      subEntity.getParent().edit();
    }

    getRepository().delete(entity);
    entity.setMode(Mode.DELETED);
    return entity;
  }

  default Page<T> searchQuery(String query, Pageable pageable) {
    return getRepository().searchQuery(query, pageable);
  }

  default Page<T> searchQuery(String query, int page, int size) {
    return searchQuery(query, PageRequest.of(page, size));
  }

  AbstractEntityRepository<T> getRepository();

  Class<T> getEntityClass();

}
