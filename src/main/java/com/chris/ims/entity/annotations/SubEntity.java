package com.chris.ims.entity.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * this is used to mark a field as a sub-entity in the database.
 * annotated fields must be of type {@link Iterable} for them to be validated
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubEntity {

}
