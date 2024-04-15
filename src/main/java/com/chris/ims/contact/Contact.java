package com.chris.ims.contact;

import com.chris.ims.entity.SpecEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * The Contact class represents a contact in the system.
 * It contains information such as the contact's name and type.
 * The type field determines whether the contact is an employee or a customer.
 */
@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "t_contact")
public class Contact extends SpecEntity {

  public static final String GROUP_EMPLOYEE = "employee";
  public static final String GROUP_CUSTOMER = "customer";

  /**
   * The type of the contact. {@link ContactType}
   */
  @Enumerated
  @Column(name = "type", nullable = false)
  private ContactType type;


  /**
   * Returns a DTO representation of the contact.
   *
   * @return The DTO representation of the contact.
   */
  public ContactDto toDto() {
    return new ContactDto(this);
  }
}