package com.ukma.tests.scope;

import com.ukma.aic.beans.BeanContext;
import com.ukma.aic.exceptions.DependencyCycleFoundException;
import com.ukma.tests.scope.classes.IDependentClass;
import org.testng.annotations.BeforeClass;
import org.testng.asserts.Assertion;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;


public class BaseTestScope extends Assertion {
    protected BeanContext beanContext;

    @BeforeClass
    public void setUpClass() throws DependencyCycleFoundException {
        beanContext = new BeanContext("singleton_prototype.xml");
    }

    protected List<String> invokeSingletonBeanMethodsInConcurrentThreadsAndGetOutput() throws InterruptedException, IOException {
        String outputFileName = "test_output";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        invokeMethodInConcurrentThreads();
        System.setOut(System.out);
        byteArrayOutputStream.writeTo(new FileOutputStream(outputFileName));
        return new BufferedReader(new FileReader(outputFileName)).lines()
                .map(line -> line.replace("\n", ""))
                .collect(Collectors.toList());
    }

    private void invokeMethodInConcurrentThreads() throws InterruptedException {
        IDependentClass firstInstance = (IDependentClass) beanContext.loadObject("firstClass");
        IDependentClass secondInstance = (IDependentClass) beanContext.loadObject("firstClass");
        createAndRunThreads(firstInstance, secondInstance);
    }
    private void createAndRunThreads(IDependentClass firstInstance, IDependentClass secondInstance) {
        try {
            createAndRunThreadsOrThrowException(firstInstance, secondInstance);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createAndRunThreadsOrThrowException(IDependentClass firstInstance, IDependentClass secondInstance) throws InterruptedException {
        Runnable firstInstanceRunnable = firstInstance::injectedOperation;
        Runnable secondInstanceRunnable = secondInstance::injectedOperation;
        Thread firstThread = new Thread(firstInstanceRunnable);
        Thread secondThread = new Thread(secondInstanceRunnable);
        firstThread.start();
        secondThread.start();
        firstThread.join();
        secondThread.join();
    }
}
