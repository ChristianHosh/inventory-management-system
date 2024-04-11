package com.chris.ims.item;

import com.chris.ims.unit.UnitFacade;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ItemService {

  private final ItemFacade itemFacade;
  private final UnitFacade unitFacade;

  @Transactional
  public ItemDto createItem(ItemRequest request) {
    Item item = itemFacade.newEntity(request);
    item.setBaseUnit(unitFacade.findById(request.getBaseUnitId()));
    item.setBasePrice(request.getBasePrice());

    return itemFacade.save(item).toDto();
  }

  public Page<ItemDto> getItems(int page, int size, String query) {
    return itemFacade.searchQuery(query, page, size).map(Item::toDto);
  }

  public ItemDto getItem(Long id) {
    return itemFacade.findById(id).toDto();
  }

  public ItemDto updateItem(Long id, ItemRequest request) {
    Item item = itemFacade.findById(id).edit();
    item.setName(request.getName());
    item.setBaseUnit(unitFacade.findById(request.getBaseUnitId()));
    item.setBasePrice(request.getBasePrice());

    return itemFacade.save(item).toDto();
  }

  public ItemDto patchItem(Long id, ItemRequest request) {
    Item item = itemFacade.findById(id).edit();

    if (request.getName() != null)
      item.setName(request.getName());
    if (request.getBasePrice() != null)
      item.setBasePrice(request.getBasePrice());
    if (request.getBaseUnitId() != null)
      item.setBaseUnit(unitFacade.findById(request.getBaseUnitId()));

    return itemFacade.save(item).toDto();
  }

  public ItemDto deleteItem(Long id) {
    Item item = itemFacade.findById(id);
    return itemFacade.delete(item).toDto();
  }
}
