package com.chris.ims.unit;

import com.chris.ims.entity.RequireAll;
import com.chris.ims.entity.SpecRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for {@link Unit}
 */
@Getter
@Setter(AccessLevel.NONE)
public class UnitRequest extends SpecRequest {

        @NotNull(groups = RequireAll.class, message = "factor can't be null")
        @Positive(groups = RequireAll.class, message = "factor can't be negative or zero")
        private Double factor;

        private Long belongsToId;

}