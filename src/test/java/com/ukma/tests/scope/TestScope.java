package com.ukma.tests.scope;

import com.ukma.tests.scope.classes.DependentClassTwo;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static com.ukma.aic.utils.Utils.ENTERED_SECTION_MESSAGE;
import static com.ukma.aic.utils.Utils.LEFT_SECTION_MESSAGE;

public class TestScope extends BaseTestScope {

    @Test
    public void testSingletonScope() throws InterruptedException, IOException {
        List<String> testOutput = invokeSingletonBeanMethodsInConcurrentThreadsAndGetOutput();
        String leftSectionTime = "";
        String enteredSectionTime = "";
        for (String line : testOutput) {
            if (line.contains(LEFT_SECTION_MESSAGE) && line.contains("Thread-1")) {
                leftSectionTime = line.split("\t")[2];
            } else if (line.contains(ENTERED_SECTION_MESSAGE) && line.contains("Thread-2")) {
                enteredSectionTime = line.split("\t")[2];
            }
        }
        assertTrue(Long.valueOf(leftSectionTime) < Long.valueOf(enteredSectionTime),
                "Left time = " + leftSectionTime + "\nEntered time = " + enteredSectionTime);
    }

    @Test
    public void testPrototypeScope() {
        DependentClassTwo firstInstance = (DependentClassTwo) beanContext.loadObject("secondClass");
        DependentClassTwo secondInstance = (DependentClassTwo) beanContext.loadObject("secondClass");
        assert firstInstance != secondInstance;
        assert firstInstance.equals(secondInstance);
    }
}
