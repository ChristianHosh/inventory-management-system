package com.chris.ims.unit;

import com.chris.ims.entity.SpecEntityDto;
import lombok.Getter;

import java.util.Objects;

@Getter
public class UnitDto extends SpecEntityDto {

  private final Double factor;
  private final Long belongsToId;

  public UnitDto(Unit entity) {
    super(entity);
    this.factor = entity.getFactor();

    Unit belongsTo = entity.getBelongsTo();
    this.belongsToId = Objects.nonNull(belongsTo) ? belongsTo.getId() : null;
  }
}
