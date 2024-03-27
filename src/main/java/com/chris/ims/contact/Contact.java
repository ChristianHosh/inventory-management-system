package com.chris.ims.contact;

import com.chris.ims.entity.SpecEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "t_contact")
public class Contact extends SpecEntity {

  public static final String GROUP_EMPLOYEE = "employee";
  public static final String GROUP_CUSTOMER = "customer";

  @Column(name = "is_employee")
  private Boolean isEmployee;

  public ContactDto toDto() {
    return new ContactDto(this);
  }
}