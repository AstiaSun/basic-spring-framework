package com.ukma.tests.autowired.classes;

public class DependentClassTwo implements IDependentClass {
    private String message = "Two...";
    public void injectedOperation() {
        System.out.println(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
