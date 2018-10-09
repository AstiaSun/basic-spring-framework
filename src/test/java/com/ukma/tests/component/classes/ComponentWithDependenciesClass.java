package com.ukma.tests.component.classes;

import com.ukma.aic.annotations.Autowired;
import com.ukma.aic.annotations.Component;

@Component(name = "componentWithDependencies")
public class ComponentWithDependenciesClass {
    private ComponentClass componentClass;
    private ControllerClass controllerClass;
    @Autowired
    private RepositoryClass repositoryClass;

    @Autowired
    public ComponentWithDependenciesClass(ComponentClass componentClass) {
        this.componentClass = componentClass;
    }

    @Autowired
    public void setControllerClass(ControllerClass controllerClass) {
        this.controllerClass = controllerClass;
    }

    public ComponentClass getComponentClass() {
        return componentClass;
    }

    public ControllerClass getControllerClass() {
        return controllerClass;
    }

    public RepositoryClass getRepositoryClass() {
        return repositoryClass;
    }
}
