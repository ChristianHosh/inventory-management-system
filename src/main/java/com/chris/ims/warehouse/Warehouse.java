package com.chris.ims.warehouse;

import com.chris.ims.entity.SpecEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_warehouse")
public class Warehouse extends SpecEntity {

  @Column(name = "allow_negative_stock")
  private Boolean allowNegativeStock;

}