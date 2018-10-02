package com.ukma.aic.beans;

import com.ukma.aic.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DependencyLoader {
    private Class<?> aClass;
    private HashMap<String, Bean> beans;

    public DependencyLoader(HashMap<String, Bean> beans) {
        this.beans = beans;
    }

    public List<Bean> getDependencies(Bean bean) {
        List<Bean> dependencies = new LinkedList<>();
        aClass = bean.getBeanType();
        dependencies.addAll(getDependenciesByConstructor());
        dependencies.addAll(getDependenciesByAnnotatedMethods());
        dependencies.addAll(getDependenciesByAnnotatedFields());
        return dependencies;
    }

    private List<Bean> getDependenciesByConstructor() {
        for (Constructor constructor : aClass.getConstructors()) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                return getDependenciesByConstructor(constructor);
            }
        }
        return Collections.emptyList();
    }

    private List<Bean> getDependenciesByConstructor(Constructor constructor) {
        String beanName = constructor.getDeclaredAnnotation(Autowired.class).name();
        if (beanName.equals("")) {
            return getDependenciesByType(constructor);
        } else {
            return Collections.singletonList(getDependencyByName(beanName));
        }
    }

    private Bean getDependencyByName(String beanName) {
        try {
            return getDependencyByNameOrThrowException(beanName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bean getDependencyByNameOrThrowException(String beanName) throws Exception {
        if (!beans.containsKey(beanName)) {
            throw new Exception("com.ukma.aic.beans.Bean with name='" + beanName + "' is not registered in configuration file");
        }
        return beans.get(beanName);
    }

    private List<Bean> getDependenciesByType(Constructor constructor) {
        Class[] parameterTypes = constructor.getParameterTypes();
        return findBeansToInject(parameterTypes);
    }

    private List<Bean> getDependenciesByAnnotatedMethods() {
        List<Bean> dependencies = new LinkedList<>();
        for (Method method : getAnnotatedMethods()) {
            String beanName = method.getAnnotation(Autowired.class).name();
            if (beanName.equals("")) {
                dependencies.addAll(getDependenciesByType(method));
            } else {
                dependencies.add(getDependencyByName(beanName));
            }
        }
        return dependencies;
    }

    private List<Bean> getDependenciesByAnnotatedFields() {
        List<Bean> dependencies = new LinkedList<>();
        for (Field field : getAnnotatedFields()) {
            String beanName = field.getAnnotation(Autowired.class).name();
            if (beanName.equals("")) {
                dependencies.add(getDependencyByType(field));
            } else {
                dependencies.add(getDependencyByName(beanName));
            }
        }
        return dependencies;
    }

    private Bean getDependencyByType(Field field) {
        return selectBestBean(field.getType());
    }

    private List<Bean> getDependenciesByType(Method method) {
        List<Bean> dependencies = new LinkedList<>();
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> parameterType: parameterTypes) {
            dependencies.add(selectBestBean(parameterType));
        }
        return dependencies;
    }

    private Bean selectBestBean(Class<?> parameterType) {
        Bean selectedBean = null;
        for (Bean bean : beans.values()) {
            if (parameterType.equals(bean.getBeanType())) {
                return bean;
            } else if ((selectedBean == null) && (parameterType.isAssignableFrom(bean.getBeanType()))) {
                selectedBean = bean;
            }
        }
        return selectedBean;
    }

    private List<Bean> findBeansToInject(Class<?>[] parameterTypes) {
        List<Bean> parametersToInject = new LinkedList<>();
        for (Class<?> parameterType: parameterTypes) {
            Bean bean = selectBestBean(parameterType);
            parametersToInject.add(bean);
        }
        return parametersToInject;
    }

    private List<Method> getAnnotatedMethods() {
        List<Method> annotatedMethods = new LinkedList<>();
        for (Method method : aClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Autowired.class)) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    private List<Field> getAnnotatedFields() {
        List<Field> annotatedFields = new LinkedList<>();
        for (Field field : aClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields;
    }
}
