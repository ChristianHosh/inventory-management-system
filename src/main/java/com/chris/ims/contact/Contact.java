package com.chris.ims.contact;

import com.chris.ims.entity.SpecEntity;
import com.chris.ims.entity.annotations.Res;
import com.chris.ims.entity.utils.CResources;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.NONE)
@Setter(AccessLevel.NONE)
@Entity
@Table(name = "t_contact")
public class Contact extends SpecEntity {

  public static final int F_TYPE = CResources.create("type");

  public static final String GROUP_EMPLOYEE = "employee";
  public static final String GROUP_CUSTOMER = "customer";

  @Res("type")
  @Enumerated
  @Column(name = "type", nullable = false)
  private ContactType type;

  public ContactDto toDto() {
    return new ContactDto(this);
  }
}