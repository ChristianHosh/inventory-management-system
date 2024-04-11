package com.chris.ims.entity.user;

import com.chris.ims.entity.RequireAll;
import com.chris.ims.entity.SpecRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for {@link User}
 */

@Getter
@Setter(AccessLevel.NONE)
public class UserRequest extends SpecRequest {

  @NotNull(groups = RequireAll.class, message = "username can't be null")
  @NotBlank(groups = RequireAll.class, message = "username can't be blank")
  private String username;

  @NotNull(groups = RequireAll.class, message = "password can't be null")
  @NotBlank(groups = RequireAll.class, message = "password can't be blank")
  private String password;

}