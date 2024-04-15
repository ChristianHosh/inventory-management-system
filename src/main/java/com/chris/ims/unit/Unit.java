package com.chris.ims.unit;

import com.chris.ims.entity.SpecEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Entity class for unit
 */
@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "t_unit")
public class Unit extends SpecEntity {

  /**
   * The factor of the unit
   */
  @Column(name = "factor")
  private Double factor;

  /**
   * The parent unit of the current unit
   */
  @ManyToOne
  @JoinColumn(name = "belongs_to_id")
  private Unit belongsTo;

  /**
   * Convert the current unit to a UnitDto
   *
   * @return the UnitDto
   */
  public UnitDto toDto() {
    return new UnitDto(this);
  }

  /**
   * Calculate the total factor of the unit and its parent units
   *
   * @return the total factor
   */
  public Double getTotalFactor() {
    return belongsTo == null ? factor : factor * belongsTo.getTotalFactor();
  }
}