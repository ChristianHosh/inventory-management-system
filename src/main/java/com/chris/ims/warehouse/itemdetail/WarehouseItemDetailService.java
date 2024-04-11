package com.chris.ims.warehouse.itemdetail;

import com.chris.ims.item.ItemFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WarehouseItemDetailService {

  private final WarehouseItemDetailFacade warehouseItemDetailFacade;
  private final ItemFacade itemFacade;

  public WarehouseItemDetailDto getWarehouseItemDetail(Long id) {
    return warehouseItemDetailFacade.findById(id).toDto();
  }

  public WarehouseItemDetailDto updateWarehouseItemDetail(Long id, WarehouseItemDetailRequest request) {
    WarehouseItemDetail itemDetail = warehouseItemDetailFacade.findById(id).edit();
    itemDetail.setItem(itemFacade.findById(request.itemId()));
    itemDetail.setQuantity(request.quantity());

    return warehouseItemDetailFacade.save(itemDetail).toDto();
  }

  public WarehouseItemDetailDto patchWarehouseItemDetail(Long id, WarehouseItemDetailRequest request) {
    WarehouseItemDetail itemDetail = warehouseItemDetailFacade.findById(id).edit();

    if (request.itemId() != null)
      itemDetail.setItem(itemFacade.findById(request.itemId()));
    if (request.quantity() != null)
      itemDetail.setQuantity(request.quantity());

    return warehouseItemDetailFacade.save(itemDetail).toDto();
  }

  public WarehouseItemDetailDto deleteWarehouseItemDetail(Long id) {
    WarehouseItemDetail itemDetail = warehouseItemDetailFacade.findById(id);
    return warehouseItemDetailFacade.delete(itemDetail).toDto();
  }
}
