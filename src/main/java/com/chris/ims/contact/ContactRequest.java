package com.chris.ims.contact;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * Request DTO for {@link Contact}
 */
record ContactRequest(
        @NotBlank(message = "code can't be blank")
        String code,

        @NotBlank(message = "name can't be blank")
        String name,

        Boolean isEmployee
) implements Serializable {

}