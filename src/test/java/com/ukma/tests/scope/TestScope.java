package com.ukma.tests.scope;

import com.ukma.aic.beans.BeanContext;
import com.ukma.aic.exceptions.DependencyCycleFoundException;
import com.ukma.tests.autowired.classes.DependentClassOne;
import com.ukma.tests.autowired.classes.DependentClassTwo;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestScope {
    private BeanContext beanContext;

    @BeforeClass
    public void setUpClass() throws DependencyCycleFoundException {
        beanContext = new BeanContext("singleton_prototype.xml");
    }
    @Test
    public void testSingletonScope() {
        DependentClassOne firstInstance = (DependentClassOne) beanContext.loadObject("firstClass");
        DependentClassOne secondInstance = (DependentClassOne) beanContext.loadObject("firstClass");
        assert firstInstance == secondInstance;
    }

    @Test
    public void testPrototypeScope() {
        DependentClassTwo firstInstance = (DependentClassTwo) beanContext.loadObject("secondClass");
        DependentClassTwo secondInstance = (DependentClassTwo) beanContext.loadObject("secondClass");
        assert firstInstance != secondInstance;
        assert firstInstance.equals(secondInstance);
    }
}
