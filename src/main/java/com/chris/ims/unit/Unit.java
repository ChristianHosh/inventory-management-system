package com.chris.ims.unit;

import com.chris.ims.entity.SpecEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_unit")
public class Unit extends SpecEntity {

  public static final String GROUP_HEADER = "header";

  @Column(name = "factor")
  private Double factor;

  @ManyToOne
  @JoinColumn(name = "belongs_to_id")
  private Unit belongsTo;

}