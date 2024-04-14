package com.chris.ims.entity;

import com.chris.ims.entity.exception.BxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@SuppressWarnings("unused")
public interface AbstractEntityFacade<T extends AbstractEntity> {

  Logger log = LoggerFactory.getLogger(AbstractEntityFacade.class);

  default T newEntity() {
    try {
      T entity = getEntityClass().getConstructor().newInstance();
      entity.setMode(Mode.NEW);
      return entity;
    } catch (NoSuchMethodException e) {
      log.error("could not instantiate entity of " + getEntityClass().getSimpleName() + ": " + e.getMessage(), e);
      log.error("entity " + getEntityClass().getSimpleName() + " must have a no-args constructor");
      throw BxException.unexpected(e);
    } catch (Exception e) {
      log.error("could not instantiate entity of " + getEntityClass().getSimpleName() + ": " + e.getMessage(), e);
      throw BxException.unexpected(e);
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
  
  default Page<T> findAll(Pageable pageable) {
    return getRepository().findAll(pageable);
  }
  
  default Page<T> findAll(int page, int size) {
    return findAll(PageRequest.of(page, size));
  }
  
  default boolean existsById(Long id) {
    return getRepository().existsById(id);
  }

  default boolean existsById(Iterable<Long> ids) {
    return getRepository().existsByIds(ids);
  }
  
  default long count() {
    return getRepository().count();
  }
  
  default List<T> findAllById(Iterable<Long> ids) {
    return getRepository().findAllById(ids);
  }
  
  default List<T> findAllById(Long... ids) {
    return findAllById(List.of(ids));
  }
  
  default Page<T> searchQuery(String query, Pageable pageable) {
    if (query == null) query = "";
    return getRepository().searchQuery(query, pageable);
  }

  default Page<T> searchQuery(String query, int page, int size) {
    return searchQuery(query, PageRequest.of(page, size));
  }

  AbstractEntityRepository<T> getRepository();

  Class<T> getEntityClass();

}
