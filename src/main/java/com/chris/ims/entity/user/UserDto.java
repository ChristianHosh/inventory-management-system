package com.chris.ims.entity.user;

import com.chris.ims.entity.SpecEntityDto;
import lombok.Getter;

/**
 * Response DTO for {@link User}
 */
@Getter
public class UserDto extends SpecEntityDto {

  private final String username;

  protected UserDto(User entity) {
    super(entity);
    this.username = entity.getString(User.F_USERNAME);
  }
}
