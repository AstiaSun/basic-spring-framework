package com.ukma.aic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The @Repository annotation is a specialization of the @Component annotation with
 * similar use and functionality. In addition to importing the DAOs into the DI
 * container, it also makes the unchecked exceptions (thrown from DAO methods)
 * eligible for translation into DataAccessException.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface Repository {
    String name() default"";
}
