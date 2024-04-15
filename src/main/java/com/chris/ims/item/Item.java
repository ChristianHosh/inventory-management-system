package com.chris.ims.item;

import com.chris.ims.entity.SpecEntity;
import com.chris.ims.unit.Unit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This class represents an item in the inventory management system.
 * It contains information about the base unit, base price, and other details.
 */
@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "t_item")
public class Item extends SpecEntity {

  /**
   * The base unit of the item.
   */
  @ManyToOne
  @JoinColumn(name = "base_unit_id")
  private Unit baseUnit;

  /**
   * The base price of the item.
   */
  @Column(name = "base_price", nullable = false)
  private Double basePrice;

  /**
   * Converts the item to a DTO.
   *
   * @return the DTO representation of the item
   */
  public ItemDto toDto() {
    return new ItemDto(this);
  }
}