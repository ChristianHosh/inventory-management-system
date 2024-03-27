package com.chris.ims.warehouse;

import com.chris.ims.entity.AbstractEntity;
import com.chris.ims.entity.Keyword;
import com.chris.ims.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_warehouse_item_detail")
public class WarehouseItemDetail extends AbstractEntity {

  @Keyword
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  @Column(name = "quantity", nullable = false)
  private Double quantity;

}