package com.ukma.aic.utils;

public enum Constants {
    beanIds("bean[@id]"), beanClasses("bean[@class]"), componentContainers("context:component-scan[@base-package]");

    private final String name;

    Constants(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return name;
    }
}
