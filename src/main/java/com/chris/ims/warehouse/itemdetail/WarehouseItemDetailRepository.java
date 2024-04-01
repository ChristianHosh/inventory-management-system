package com.chris.ims.warehouse.itemdetail;

import com.chris.ims.entity.AbstractEntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
interface WarehouseItemDetailRepository extends AbstractEntityRepository<WarehouseItemDetail> {

  @Query("""
    select i from WarehouseItemDetail i
    where i.warehouse.id = :id and
    i.keyword ilike concat('%', :query, '%')
  """)
  Page<WarehouseItemDetail> searchByWarehouse(Long id, String query, Pageable pageable);
}