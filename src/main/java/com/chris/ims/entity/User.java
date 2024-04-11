package com.chris.ims.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_user")
public class User extends SpecEntity {
  
  @Column(name = "username", length = 30)
  private String username;

  @Column(name = "password", length = 100)
  private String password;

}