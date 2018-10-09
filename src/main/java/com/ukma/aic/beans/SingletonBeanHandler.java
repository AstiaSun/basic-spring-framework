package com.ukma.aic.beans;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

import static com.ukma.aic.utils.Utils.ENTERED_SECTION_MESSAGE;
import static com.ukma.aic.utils.Utils.LEFT_SECTION_MESSAGE;

public class SingletonBeanHandler implements InvocationHandler {
    private Object beanObject;
    private final HashMap<String, Method> methodHashMap = new HashMap<>();
    private static final Object mutex = new Object();

    SingletonBeanHandler(Object beanObject) {
        this.beanObject = beanObject;
        for (Method method : beanObject.getClass().getDeclaredMethods()) {
            methodHashMap.put(method.getName(), method);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        synchronized (mutex) {
            System.out.println(Thread.currentThread().getName() + ENTERED_SECTION_MESSAGE + System.nanoTime());
            result = methodHashMap.get(method.getName()).invoke(beanObject, args);
        }
        System.out.println(Thread.currentThread().getName() + LEFT_SECTION_MESSAGE + System.nanoTime());
        return result;
    }
}
