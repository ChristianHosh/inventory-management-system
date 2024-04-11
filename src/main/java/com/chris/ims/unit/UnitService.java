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
    if (request.getBelongsToId() != null) {
      parentUnit = unitFacade.findById(request.getBelongsToId());
    }

    Unit unit = unitFacade.newEntity(request);
    unit.setBelongsTo(parentUnit);
    unit.setFactor(request.getFactor());

    return unitFacade.save(unit).toDto();
  }

  public UnitDto getUnit(Long id) {
    return unitFacade.findById(id).toDto();
  }

  public UnitDto updateUnit(Long id, UnitRequest request) {
    Unit parentUnit = null;
    if (request.getBelongsToId() != null) {
      parentUnit = unitFacade.findById(request.getBelongsToId());
    }

    Unit unit = unitFacade.findById(id).edit();
    unit.setBelongsTo(parentUnit);
    unit.setFactor(request.getFactor());
    unit.setName(request.getName());

    return unitFacade.save(unit).toDto();
  }

  public UnitDto patchUnit(Long id, UnitRequest request) {
    Unit unit = unitFacade.findById(id).edit();

    if (request.getName() != null)
      unit.setName(request.getName());
    if (request.getFactor() != null)
      unit.setFactor(request.getFactor());
    if (request.getBelongsToId() != null)
      unit.setBelongsTo(unitFacade.findById(request.getBelongsToId()));

    return unitFacade.save(unit).toDto();
  }

  public UnitDto deleteUnit(Long id) {
    Unit unit = unitFacade.findById(id);
    return unitFacade.delete(unit).toDto();
  }
}
