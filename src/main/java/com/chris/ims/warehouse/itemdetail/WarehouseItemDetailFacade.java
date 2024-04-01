package com.chris.ims.warehouse.itemdetail;

import com.chris.ims.entity.AbstractEntityFacade;
import com.chris.ims.entity.AbstractEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WarehouseItemDetailFacade implements AbstractEntityFacade<WarehouseItemDetail> {

  private final WarehouseItemDetailRepository repository;

  @Override
  public AbstractEntityRepository<WarehouseItemDetail> getRepository() {
    return repository;
  }

  @Override
  public Class<WarehouseItemDetail> getEntityClass() {
    return WarehouseItemDetail.class;
  }

  public Page<WarehouseItemDetail> searchQueryByWarehouse(Long id, int page, int size, String query) {
    return repository.searchByWarehouse(id, query, PageRequest.of(page, size));
  }
}
