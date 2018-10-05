package com.ukma.tests.autowired.classes;

public class DependentClassOne implements IDependentClass {
    private String message = "One...";

    public void injectedOperation() {
        System.out.println(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
