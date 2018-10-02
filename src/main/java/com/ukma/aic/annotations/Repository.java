package com.ukma.aic.annotations;

/**
 * The @Repository annotation is a specialization of the @Component annotation with
 * similar use and functionality. In addition to importing the DAOs into the DI
 * container, it also makes the unchecked exceptions (thrown from DAO methods)
 * eligible for translation into DataAccessException.
 */
public @interface Repository {
    String name() default"";
}
