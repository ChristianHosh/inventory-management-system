package com.chris.ims.warehouse;

import com.chris.ims.entity.SpecEntity;
import com.chris.ims.entity.annotations.SubEntity;
import com.chris.ims.warehouse.itemdetail.WarehouseItemDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class represents a warehouse in the inventory management system.
 */
@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "t_warehouse")
public class Warehouse extends SpecEntity {

  /**
   * Indicates whether negative stock is allowed for this warehouse.
   */
  @Column(name = "allow_negative_stock")
  private Boolean allowNegativeStock;

  /**
   * The set of item details associated with this warehouse.
   */
  @SubEntity
  @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<WarehouseItemDetail> itemDetails = new LinkedHashSet<>();

  /**
   * Converts this warehouse to a DTO.
   *
   * @return the warehouse DTO
   */
  public WarehouseDto toDto() {
    return new WarehouseDto(this);
  }
}