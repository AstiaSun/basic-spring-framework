package com.ukma.aic.beans;

public class Bean {
    private Class<?> beanType;
    private String name;
    private Object bean;
    private BeanCategory beanCategory;

    Bean(String name, Class<?> beanType) {
        this.name = name;
        this.beanType = beanType;
        this.beanCategory = BeanCategory.COMPONENT;
    }

    Bean(String name, Class<?> beanType, BeanCategory beanCategory) {
        this.name = name;
        this.beanType = beanType;
        this.beanCategory = beanCategory;
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

    public BeanCategory getBeanCategory() {
        return beanCategory;
    }
}
