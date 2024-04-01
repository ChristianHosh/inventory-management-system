package com.chris.ims.item;

import com.chris.ims.entity.SpecEntity;
import com.chris.ims.unit.Unit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "t_item")
public class Item extends SpecEntity {

  @ManyToOne
  @JoinColumn(name = "base_unit_id")
  private Unit baseUnit;

  @Column(name = "base_price", nullable = false)
  private Double basePrice;

  public ItemDto toDto() {
    return new ItemDto(this);
  }
}