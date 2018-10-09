package com.ukma.aic.beans;

public class Bean {
    private Class<?> beanType;
    private String name;
    private Object bean;
    private BeanCategory beanCategory;
    private BeanScope beanScope;

    Bean(String name, Class<?> beanType) {
        this.name = name;
        this.beanType = beanType;
        this.beanCategory = BeanCategory.COMPONENT;
        this.beanScope = BeanScope.SINGLETON;
    }

    Bean(String name, Class<?> beanType, BeanCategory beanCategory) {
        this.name = name;
        this.beanType = beanType;
        this.beanCategory = beanCategory;
        this.beanScope = BeanScope.SINGLETON;
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

    BeanScope getBeanScope() {
        return beanScope;
    }

    void setBeanScope(BeanScope beanScope) {
        this.beanScope = beanScope;
    }
}
