package com.chris.ims.item;

import com.chris.ims.entity.EntField;
import com.chris.ims.entity.SpecEntity;
import com.chris.ims.entity.annotations.Res;
import com.chris.ims.entity.exception.BxException;
import com.chris.ims.entity.utils.CResources;
import com.chris.ims.unit.Unit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Res("item")
@Table(name = "t_item")
public class Item extends SpecEntity {

  public static final int F_BASE_UNIT = CResources.create("baseUnit");
  public static final int F_BASE_PRICE = CResources.create("basePrice");

  @ManyToOne
  @Res("baseUnit")
  @JoinColumn(name = "base_unit_id", nullable = false)
  private Unit baseUnit;

  @Res("basePrice")
  @Column(name = "base_price", nullable = false)
  private Double basePrice;


  @Override
  public void validate(EntField field) {
    super.validate(field);

    if (field.isField(F_BASE_UNIT) && getEntity(field).isNull(Unit.F_BELONGS_TO)) {
      throw BxException.badRequest(getClass(), "base unit must be a header");
    }
  }

  public ItemDto toDto() {
    return new ItemDto(this);
  }
}