package com.chris.ims.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * This interface defines the common methods for all data access objects (DAOs) that interact with the database.
 *
 * @param <T> the type of entity that the DAO manages
 */
@NoRepositoryBean
public interface AbstractEntityRepository<T extends AbstractEntity> extends JpaRepository<T, Long> {

  /**
   * Searches for entities based on a keyword search.
   *
   * @param query    the keyword to search for
   * @param pageable the pagination information
   * @return a page of entities that match the search criteria
   */
  @Query("""
          select e from #{#entityName} e
          where e.keyword ilike concat('%', :query, '%')
          """)
  Page<T> searchQuery(String query, Pageable pageable);

}
