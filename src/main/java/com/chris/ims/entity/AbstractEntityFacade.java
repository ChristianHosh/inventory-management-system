package com.chris.ims.entity;

import com.chris.ims.entity.exception.BxException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * This interface provides basic operations for managing entities.
 *
 * @param <T> the type of entity
 */
public interface AbstractEntityFacade<T extends AbstractEntity> {

  /**
   * Finds an entity by its ID.
   *
   * @param id the ID of the entity
   * @return the entity with the given ID
   * @throws BxException if the entity does not exist
   */
  default T findById(Long id) {
    return getRepository().findById(id)
            .orElseThrow(BxException.xNotFound(getEntityClass(), id));
  }

  /**
   * Saves an entity.
   *
   * @param entity the entity to be saved
   * @return the saved entity
   */
  default <S extends T> T save(S entity) {
    entity.validate();
    return getRepository().save(entity);
  }

  /**
   * Deletes an entity.
   *
   * @param entity the entity to be deleted
   * @return the deleted entity
   */
  default <S extends T> S delete(S entity) {
    getRepository().delete(entity);
    return entity;
  }

  /**
   * Finds a page of entities.
   *
   * @param page the page number
   * @param size the number of entities per page
   * @return a page of entities
   */
  default Page<T> findPage(int page, int size) {
    return findPage(PageRequest.of(page, size));
  }

  /**
   * Finds a page of entities.
   *
   * @param pageable the page information
   * @return a page of entities
   */
  default Page<T> findPage(Pageable pageable) {
    return getRepository().findAll(pageable);
  }

  /**
   * Searches for entities using a query.
   *
   * @param query    the query string
   * @param pageable the page information
   * @return a page of entities that match the query
   */
  default Page<T> searchQuery(String query, Pageable pageable) {
    return getRepository().searchQuery(query, pageable);
  }

  /**
   * Searches for entities using a query.
   *
   * @param query the query string
   * @param page  the page number
   * @param size  the number of entities per page
   * @return a page of entities that match the query
   */
  default Page<T> searchQuery(String query, int page, int size) {
    return searchQuery(query, PageRequest.of(page, size));
  }

  /**
   * Returns the repository for the entity type.
   *
   * @return the repository
   */
  AbstractEntityRepository<T> getRepository();

  /**
   * Returns the class of the entity type.
   *
   * @return the class
   */
  Class<T> getEntityClass();
}