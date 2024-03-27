package com.chris.ims.contact;

import com.chris.ims.entity.SpecEntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
interface ContactRepository extends SpecEntityRepository<Contact> {

  @Query("select c from Contact c where c.isEmployee = true and :query ilike concat('%', c.keyword ,'%') ")
  Page<Contact> searchQueryIsEmployee(String query, Pageable pageable);

  @Query("select c from Contact c where c.isEmployee = false and :query ilike concat('%', c.keyword ,'%') ")
  Page<Contact> searchQueryIsCustomer(String query, Pageable pageable);

}