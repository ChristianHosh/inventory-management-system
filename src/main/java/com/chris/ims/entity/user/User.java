package com.chris.ims.entity.user;

import com.chris.ims.entity.SpecEntity;
import com.chris.ims.entity.annotations.Res;
import com.chris.ims.entity.utils.CResources;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.NONE)
@Setter(AccessLevel.NONE)
@Entity
@Res("user")
@Table(name = "t_user")
public class User extends SpecEntity {

  public static final int F_USERNAME = CResources.create("username");
  public static final int F_PASSWORD = CResources.create("password");

  @Res("username")
  @Column(name = "username", length = 30, unique = true)
  private String username;

  @Res("password")
  @Column(name = "password", length = 100)
  private String password;

  public UserDto toDto() {
    return new UserDto(this);
  }
  
  public boolean isAdmin() {
    return "ADMIN".equalsIgnoreCase(this.username);
  }
}