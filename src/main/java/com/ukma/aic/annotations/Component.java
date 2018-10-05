package com.ukma.aic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation @Component marks a java class as a bean so the scanning mechanism
 * can pick it up and pull it into the bean context.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface Component {
    String name() default "";
}
