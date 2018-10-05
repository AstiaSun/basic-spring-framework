package com.ukma.tests.autowired;

import com.ukma.aic.beans.BeanContext;
import com.ukma.aic.exceptions.DependencyCycleFoundException;
import com.ukma.tests.autowired.classes.AutowiredClass;
import com.ukma.tests.autowired.classes.AutowiredNameClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestAutowiredAnnotation {
    private BeanContext beanContext;
    @BeforeClass
    private void setUpClass() throws DependencyCycleFoundException {
        beanContext = new BeanContext("dependencies.xml");
    }
    @Test
    public void testSimpleAutowiredAnnotation() {
        AutowiredClass testClass = (AutowiredClass) beanContext.loadObject("simpleAutowired");
        assert testClass != null;
        assert testClass.getFirstClass() != null;
        assert testClass.getSecondClass() != null;
        assert testClass.getThirdClass() != null;
    }

    @Test
    public void  testAutomwiredAnnotationWithName() {
        AutowiredNameClass testClass = (AutowiredNameClass) beanContext.loadObject("parametereziedAutowired");
        assert testClass != null;
        assert testClass.getFirstClass() != null;
        assert testClass.getSecondClass() != null;
        assert testClass.getThirdClass() != null;
    }
}