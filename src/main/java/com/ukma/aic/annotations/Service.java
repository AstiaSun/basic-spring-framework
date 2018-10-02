package com.ukma.aic.annotations;

/**
 * The @Service annotation is also a specialization of the component annotation.
 * It does not currently provide any additional behavior over the @Component
 * annotation, but it’s a good idea to use @Service over @Component in service-layer
 * classes because it specifies intent better. Additionally, tool support and
 * additional behavior might rely on it in the future.
 */
public @interface Service {
    String name() default "";
}
