package com.ukma.tests.autowired.classes;

public class DependentClassThree implements IDependentClass {
    @Override
    public void injectedOperation() {
        System.out.println("Hello, World!");
    }
}
