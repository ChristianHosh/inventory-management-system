package com.chris.ims.unit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UnitService {

  private final UnitFacade unitFacade;

  public Page<UnitDto> getUnits(int page, int size, String query) {
    return unitFacade.searchQuery(query, page, size).map(Unit::toDto);
  }

  public UnitDto createUnit(UnitRequest request) {
    Unit parentUnit = null;
    if (request.belongsToId() != null) {
      parentUnit = unitFacade.findById(request.belongsToId());
    }

    Unit unit = new Unit()
            .setBelongsTo(parentUnit)
            .setFactor(request.factor())
            .setName(request.name());

    return unitFacade.save(unit).toDto();
  }

  public UnitDto getUnit(Long id) {
    return unitFacade.findById(id).toDto();
  }

  public UnitDto updateUnit(Long id, UnitRequest request) {
    Unit parentUnit = null;
    if (request.belongsToId() != null) {
      parentUnit = unitFacade.findById(request.belongsToId());
    }

    Unit unit = unitFacade.findById(id)
            .setBelongsTo(parentUnit)
            .setFactor(request.factor())
            .setName(request.name());

    return unitFacade.save(unit).toDto();
  }

  public UnitDto patchUnit(Long id, UnitRequest request) {
    Unit unit = unitFacade.findById(id);

    if (request.name() != null)
      unit.setName(request.name());
    if (request.factor() != null)
      unit.setFactor(request.factor());
    if (request.belongsToId() != null)
      unit.setBelongsTo(unitFacade.findById(request.belongsToId()));

    return unitFacade.save(unit).toDto();
  }

  public UnitDto deleteUnit(Long id) {
    Unit unit = unitFacade.findById(id);
    return unitFacade.delete(unit).toDto();
  }
}
