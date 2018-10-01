package com.ukma.aic.beans;

public class Bean {
    private Class<?> beanType;
    private String name;
    private Object bean;

    Bean(String name, Class<?> beanType) {
        this.name = name;
        this.beanType = beanType;
    }

    Class<?> getBeanType() {
        return beanType;
    }

    public String getName() {
        return name;
    }

    Object getBean() {
        return bean;
    }

    void setBean(Object bean) {
        this.bean = bean;
    }
}
