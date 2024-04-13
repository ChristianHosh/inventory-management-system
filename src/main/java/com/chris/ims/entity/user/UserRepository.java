package com.chris.ims.entity.user;

import com.chris.ims.entity.AbstractEntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface UserRepository extends AbstractEntityRepository<User> {

  @Query("select u from User u where u.username ilike :username")
  Optional<User> findByUsername(String username);

  @Query("select (count(u) > 0) from User u where u.username ilike :username")
  boolean existsByUsername(String username);
}