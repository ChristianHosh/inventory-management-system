package com.chris.ims.warehouse.itemdetail;

import com.chris.ims.entity.AbstractEntity;
import com.chris.ims.entity.annotations.Keyword;
import com.chris.ims.item.Item;
import com.chris.ims.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "t_warehouse_item_detail")
public class WarehouseItemDetail extends AbstractEntity {

  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  @Column(name = "quantity", nullable = false)
  private Double quantity;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
  @JoinColumn(name = "warehouse_id", nullable = false)
  private Warehouse warehouse;

  public WarehouseItemDetailDto toDto() {
    return new WarehouseItemDetailDto(this);
  }
}