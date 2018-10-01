package com.ukma.aic.exceptions;

public class DependencyCycleFoundException extends Exception {
    public DependencyCycleFoundException() {
        super("Dependency cycle has been found within beans.");
    }
}
