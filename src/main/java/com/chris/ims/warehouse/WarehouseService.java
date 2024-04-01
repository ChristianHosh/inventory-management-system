package com.chris.ims.warehouse;

import com.chris.ims.item.Item;
import com.chris.ims.item.ItemFacade;
import com.chris.ims.warehouse.itemdetail.WarehouseItemDetail;
import com.chris.ims.warehouse.itemdetail.WarehouseItemDetailDto;
import com.chris.ims.warehouse.itemdetail.WarehouseItemDetailFacade;
import com.chris.ims.warehouse.itemdetail.WarehouseItemDetailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class WarehouseService {

  private final WarehouseFacade warehouseFacade;
  private final WarehouseItemDetailFacade warehouseItemDetailFacade;
  private final ItemFacade itemFacade;

  public Page<WarehouseDto> getWarehouses(int page, int size, String query) {
    return warehouseFacade.searchQuery(query, page, size).map(Warehouse::toDto);
  }

  public WarehouseDto getWarehouse(Long id) {
    return warehouseFacade.findById(id).toDto();
  }

  public WarehouseDto createWarehouse(WarehouseRequest request) {
    Warehouse warehouse = new Warehouse()
            .setAllowNegativeStock(request.allowNegativeStock())
            .setName(request.name());

    return warehouseFacade.save(warehouse).toDto();
  }

  public WarehouseDto updateWarehouse(Long id, WarehouseRequest request) {
    Warehouse warehouse = warehouseFacade.findById(id)
            .setAllowNegativeStock(request.allowNegativeStock())
            .setName(request.name());

    return warehouseFacade.save(warehouse).toDto();
  }

  public WarehouseDto patchWarehouse(Long id, WarehouseRequest request) {
    Warehouse warehouse = warehouseFacade.findById(id);

    if (request.name() != null)
      warehouse.setName(request.name());
    if (request.allowNegativeStock() != null)
      warehouse.setAllowNegativeStock(request.allowNegativeStock());

    return warehouseFacade.save(warehouse).toDto();
  }

  public Page<WarehouseItemDetailDto> getWarehouseDetails(Long id, int page, int size, String query) {
    return warehouseItemDetailFacade.searchQueryByWarehouse(id, page, size, query).map(WarehouseItemDetail::toDto);
  }

  public WarehouseItemDetailDto createWarehouseDetail(Long id, WarehouseItemDetailRequest request) {
    Warehouse warehouse = warehouseFacade.findById(id);
    Item item = itemFacade.findById(request.itemId());

    WarehouseItemDetail warehouseItemDetail = new WarehouseItemDetail()
            .setItem(item)
            .setQuantity(request.quantity())
            .setWarehouse(warehouse);

    return warehouseItemDetailFacade.save(warehouseItemDetail).toDto();
  }

  public WarehouseDto deleteWarehouse(Long id) {
    Warehouse warehouse = warehouseFacade.findById(id);
    return warehouseFacade.delete(warehouse).toDto();
  }

}
