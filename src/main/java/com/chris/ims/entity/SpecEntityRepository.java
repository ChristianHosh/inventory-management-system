package com.chris.ims.entity;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface SpecEntityRepository<T extends SpecEntity> extends AbstractEntityRepository<T> {

  Optional<T> findByCode(String code);

  boolean existsByCode(String code);
}
