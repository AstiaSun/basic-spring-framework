package com.ukma.tests.component;

import com.ukma.aic.beans.BeanContext;
import com.ukma.aic.exceptions.DependencyCycleFoundException;
import com.ukma.tests.component.classes.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestComponentAnnotation {
    private BeanContext beanContext;

    @BeforeClass
    public void setUpClass() throws DependencyCycleFoundException {
        beanContext = new BeanContext("components.xml");
    }

    @Test
    public void testLoadComponentClass() {
        ComponentClass testClass = (ComponentClass) beanContext.loadObject("componentClass");
        assert testClass != null;
    }

    @Test
    public void testLoadServiceClass() {
        ServiceClass testClass = (ServiceClass) beanContext.loadObject("serviceClass");
        assert testClass != null;
    }

    @Test
    public void testLoadControllerClass() {
        ControllerClass testClass = (ControllerClass) beanContext.loadObject("controllerClass");
        assert testClass != null;
    }

    @Test
    public void testLoadRepositoryClass() {
        RepositoryClass testClass = (RepositoryClass) beanContext.loadObject("repositoryClass");
        assert testClass != null;
    }

    @Test
    public void testComponentWithDependenciesClass() {
        ComponentWithDependenciesClass componentClass = (ComponentWithDependenciesClass) beanContext.loadObject("componentWithDependencies");
        assert componentClass != null;
        assert componentClass.getComponentClass() != null;
        assert componentClass.getControllerClass() != null;
        assert componentClass.getRepositoryClass() != null;
    }
}
