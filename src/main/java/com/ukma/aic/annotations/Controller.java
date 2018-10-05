package com.ukma.aic.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation @Controller marks class as a Web MVC controller. It is also a @Component
 * specialisation, so the object also will be added to bean context. When annotation @Controller
 * is added, usage of @RequestMapping is allowed to map URLs to instance of a class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface Controller {
    String name() default "";
}
