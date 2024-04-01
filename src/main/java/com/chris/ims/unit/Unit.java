package com.chris.ims.unit;

import com.chris.ims.entity.SpecEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "t_unit")
public class Unit extends SpecEntity {

  @Column(name = "factor")
  private Double factor;

  @ManyToOne
  @JoinColumn(name = "belongs_to_id")
  private Unit belongsTo;

  public UnitDto toDto() {
    return new UnitDto(this);
  }

  public Double getTotalFactor() {
    return belongsTo == null ? factor : factor * belongsTo.getTotalFactor();
  }
}