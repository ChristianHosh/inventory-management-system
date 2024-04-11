package com.chris.ims.warehouse;

import com.chris.ims.entity.SpecEntity;
import com.chris.ims.entity.annotations.SubEntityList;
import com.chris.ims.warehouse.itemdetail.WarehouseItemDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "t_warehouse")
public class Warehouse extends SpecEntity {

  @Column(name = "allow_negative_stock")
  private Boolean allowNegativeStock;

  @SubEntityList
  @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<WarehouseItemDetail> itemDetails = new LinkedHashSet<>();

  public WarehouseDto toDto() {
    return new WarehouseDto(this);
  }
}