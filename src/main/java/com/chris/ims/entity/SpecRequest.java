package com.chris.ims.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.NONE)
public abstract class SpecRequest {

  @NotNull(groups = RequireAll.class, message = "name can't be null")
  @NotBlank(groups = RequireAll.class, message = "name can't be blank")
  private String name;

}
