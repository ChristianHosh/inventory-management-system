package com.chris.ims.warehouse.itemdetail;

import com.chris.ims.entity.AbstractEntity;
import com.chris.ims.entity.annotations.Keyword;
import com.chris.ims.item.Item;
import com.chris.ims.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Entity class for WarehouseItemDetail
 */
@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "t_warehouse_item_detail")
public class WarehouseItemDetail extends AbstractEntity {

  /**
   * The item that is being stored in the warehouse
   */
  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  /**
   * The quantity of the item that is being stored
   */
  @Column(name = "quantity", nullable = false)
  private Double quantity;

  /**
   * The warehouse that the item is being stored in
   */
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
  @JoinColumn(name = "warehouse_id", nullable = false)
  private Warehouse warehouse;

  /**
   * Converts the WarehouseItemDetail object into a WarehouseItemDetailDto object
   *
   * @return WarehouseItemDetailDto object
   */
  public WarehouseItemDetailDto toDto() {
    return new WarehouseItemDetailDto(this);
  }
}