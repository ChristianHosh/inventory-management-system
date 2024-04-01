package com.chris.ims.contact;

import com.chris.ims.entity.RequireAll;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * Request DTO for {@link Contact}
 */
record ContactRequest(

        @NotNull(groups = RequireAll.class, message = "name can't be null")
        @NotBlank(groups = RequireAll.class, message = "name can't be blank")
        String name,

        @NotNull(groups = RequireAll.class, message = "type can't be null")
        @NotBlank(groups = RequireAll.class, message = "type can't be blank")
        ContactType type

) implements Serializable {

}