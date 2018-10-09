package com.ukma.tests.scope.classes;

import java.util.Objects;

public class DependentClassTwo implements IDependentClass {
    private String message = "Two...";
    public Object injectedOperation() {
        System.out.println(message);
        return null;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DependentClassTwo) {
            if (Objects.equals(((DependentClassTwo) obj).message, message)) {
                return true;
            }
        }
        return false;
    }
}
