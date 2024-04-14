package com.chris.ims.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractEntityRepository<T extends AbstractEntity> extends JpaRepository<T, Long> {

  @Query("""
          select e from #{#entityName} e
          where e.keyword ilike concat('%', :query, '%')
          """)
  Page<T> searchQuery(String query, Pageable pageable);

  @Query("""
        select (count() > 0) from #{#entityName} e
        where e.id in :ids
        """)
  boolean existsByIds(Iterable<Long> ids);
}
