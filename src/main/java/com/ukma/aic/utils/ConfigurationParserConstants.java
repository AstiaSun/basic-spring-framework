package com.ukma.aic.utils;

public enum ConfigurationParserConstants {
    beanIds("bean[@id]"), beanClasses("bean[@class]"), componentContainers("context:component-scan[@base-package]");

    private final String name;

    ConfigurationParserConstants(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return name;
    }
}
