package com.ukma.aic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The @Service annotation is also a specialization of the component annotation.
 * It does not currently provide any additional behavior over the @Component
 * annotation, but it’s a good idea to use @Service over @Component in service-layer
 * classes because it specifies intent better. Additionally, tool support and
 * additional behavior might rely on it in the future.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface Service {
    String name() default "";
}
