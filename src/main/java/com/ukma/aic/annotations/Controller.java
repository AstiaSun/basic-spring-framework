package com.ukma.aic.annotations;

/**
 * Annotation @Controller marks class as a Web MVC controller. It is also a @Component
 * specialisation, so the object also will be added to bean context. When annotation @Controller
 * is added, usage of @RequestMapping is allowed to map URLs to instance of a class.
 */
public @interface Controller {
    String name() default "";
}
