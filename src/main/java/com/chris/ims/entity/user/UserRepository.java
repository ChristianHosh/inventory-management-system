package com.chris.ims.entity.user;

import com.chris.ims.entity.AbstractEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepository extends AbstractEntityRepository<User> {
  
}