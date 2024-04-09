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

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "t_warehouse")
public class Warehouse extends SpecEntity {

  @Column(name = "allow_negative_stock")
  private Boolean allowNegativeStock;

  @SubEntity
  @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<WarehouseItemDetail> itemDetails = new LinkedHashSet<>();

  public WarehouseDto toDto() {
    return new WarehouseDto(this);
  }
}