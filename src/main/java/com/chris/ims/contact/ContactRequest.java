package com.chris.ims.contact;

import com.chris.ims.entity.RequireAll;
import com.chris.ims.entity.SpecRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for {@link Contact}
 */
@Getter
@Setter
public class ContactRequest extends SpecRequest {

  @NotNull(groups = RequireAll.class, message = "type can't be null")
  @NotBlank(groups = RequireAll.class, message = "type can't be blank")
  private ContactType type;

}
