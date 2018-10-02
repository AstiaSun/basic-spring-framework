package com.ukma.aic.beans;

import com.ukma.aic.annotation.Autowired;
import com.ukma.aic.exceptions.BeanNotFoundException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.ukma.aic.utils.Utils.newInstance;

class InstanceCreator {
    private Object instance;
    private Class instanceType;
    private HashMap<String, Bean> beans;

    InstanceCreator(HashMap<String, Bean> beans) {
        this.beans = beans;
    }

    Object createObject(Bean bean) throws BeanNotFoundException {
        init(bean);
        injectDependenciesByConstructor();
        if (instance == null) {
            instance = newInstance(instanceType);
        }
        injectDependenciesByAnnotatedMethods();
        injectDependenciesByAnnotatedFields();
        return instance;
    }

    private void init(Bean bean) {
        instance = null;
        instanceType = bean.getBeanType();
    }

    private void injectDependenciesByAnnotatedFields() {
        for (Field field : instanceType.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                injectDependenciesByAnnotatedField(field);
            }
        }
    }

    private void injectDependenciesByAnnotatedField(Field field) {
        String beanName = field.getDeclaredAnnotation(Autowired.class).name();
        if (beanName.equals("")) {
            setField(field, findObjectToInject(field.getType()));
        } else {
            setField(field, beans.get(beanName).getBean());
        }
    }

    private Object findObjectToInject(Class parameterType) {
        Bean selectedBean = null;
        for(Bean bean: beans.values()) {
            if (bean.getBean() != null) {
                if (bean.getBeanType().equals(parameterType)) {
                    selectedBean = bean;
                    break;
                } else if (bean.getBeanType().isAssignableFrom(parameterType)) {
                    selectedBean = bean;
                }
            }
        }
        return selectedBean == null ? new Object() : selectedBean.getBean();
    }

    private void injectDependenciesByAnnotatedMethods() {
        for (Method method : instanceType.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Autowired.class)) {
                injectDependenciesByAnnotatedMethod(method);
            }
        }
    }

    private void injectDependenciesByAnnotatedMethod(Method method) {
        String beanName = method.getDeclaredAnnotation(Autowired.class).name();
        List<Object> objectsToInject = new LinkedList<>();
        if (beanName.equals("")) {
            objectsToInject = findObjectToInject(method.getParameterTypes());
        } else {
            objectsToInject.add(beans.get(beanName).getBean());
        }
        invokeMethod(method, objectsToInject);
    }

    private void injectDependenciesByConstructor() throws BeanNotFoundException {
        for (Constructor constructor : instanceType.getConstructors()) {
            if(constructor.isAnnotationPresent(Autowired.class)) {
                injectDependenciesByConstructor(constructor);
            }
        }
    }

    private void injectDependenciesByConstructor(Constructor constructor) throws BeanNotFoundException {
        String beanName = constructor.getDeclaredAnnotation(Autowired.class).name();
        List<Object> dependentObjects = new LinkedList<>();
        if (beanName.equals("")) {
            dependentObjects = findObjectToInject(constructor.getParameterTypes());
        } else {
            if (!beans.containsKey(beanName)) {
                throw new BeanNotFoundException(beanName);
            }
            dependentObjects.add(beans.get(beanName).getBean());
        }
        instance = createNewInstanceWithConstructor(constructor, dependentObjects);
    }

    private List<Object> findObjectToInject(Class<?>[] parameterTypes) {
        List<Object> objectsToInject = new LinkedList<>();
        for (Class<?> parameterType: parameterTypes) {
            objectsToInject.add(findObjectToInject(parameterType));
        }
        return objectsToInject;
    }

    private void setField(Field field, Object object) {
        try {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            field.set(instance, object);
            field.setAccessible(accessible);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void invokeMethod(Method method, List<Object> params) {
        try {
            method.invoke(instance, params.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Object createNewInstanceWithConstructor(Constructor constructor, List<Object> parameters) {
        try {
            return constructor.newInstance(parameters.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return new Object();
    }
}
