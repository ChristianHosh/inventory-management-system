package com.chris.ims.contact;

import com.chris.ims.entity.AbstractEntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
interface ContactRepository extends AbstractEntityRepository<Contact> {

  @Query("""
          select c from Contact c
          where c.type = 0 and
          c.keyword ilike concat('%', :query ,'%')
          """)
  Page<Contact> searchQueryIsEmployee(String query, Pageable pageable);

  @Query("""
          select c from Contact c
          where c.type = 1 and
          c.keyword ilike concat('%', :query ,'%')
          """)
  Page<Contact> searchQueryIsCustomer(String query, Pageable pageable);

}