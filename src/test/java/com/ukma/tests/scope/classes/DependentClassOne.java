package com.ukma.tests.scope.classes;

public class DependentClassOne implements IDependentClass {
    private String message = "One...";

    public Object injectedOperation() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(message);
        return null;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
