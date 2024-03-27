package com.chris.ims.item;

import com.chris.ims.entity.SpecEntity;
import com.chris.ims.unit.Unit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_item")
public class Item extends SpecEntity {

  @ManyToOne
  @JoinColumn(name = "base_unit_id")
  private Unit baseUnit;

  @Column(name = "base_price", nullable = false)
  private Double basePrice;

}