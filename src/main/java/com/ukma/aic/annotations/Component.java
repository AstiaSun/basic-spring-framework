package com.ukma.aic.annotations;

/**
 * Annotation @Component marks a java class as a bean so the scanning mechanism
 * can pick it up and pull it into the bean context.
 */
public @interface Component {
    String name() default "";
}
