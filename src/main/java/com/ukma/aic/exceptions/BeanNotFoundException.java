package com.ukma.aic.exceptions;

public class BeanNotFoundException extends Exception {
    public BeanNotFoundException(String beanName) {
        super("Bean with name='" + beanName + "' is not found in the list of beans");
    }
}
