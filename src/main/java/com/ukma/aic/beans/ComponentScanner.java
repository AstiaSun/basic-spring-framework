package com.ukma.aic.beans;

import com.ukma.aic.annotations.Component;
import com.ukma.aic.annotations.Controller;
import com.ukma.aic.annotations.Repository;
import com.ukma.aic.annotations.Service;
import com.ukma.aic.beans.bean.category.handlers.*;
import com.ukma.aic.utils.BeanClassLoader;

import java.lang.annotation.Annotation;
import java.util.*;

public class ComponentScanner {

    private final List<Class<?>> componentAnnotations = Arrays.asList(
            Component.class, Controller.class, Repository.class, Service.class
    );
    private HashMap<Class<?>, IBeanCategoryHandler> classToCategoryMap;

    public ComponentScanner() {
        initClassToCategoryMap();
    }

    private void initClassToCategoryMap() {
        classToCategoryMap = new HashMap<>();
        classToCategoryMap.put(Component.class, new ComponentHandler());
        classToCategoryMap.put(Controller.class, new ControllerHandler());
        classToCategoryMap.put(Repository.class, new RepositoryHandler());
        classToCategoryMap.put(Service.class, new ServiceHandler());
    }

    public List<Bean> scanForAnnotatedBeans(List<String> directoriesToScan) {
        List<Class<?>> loadClassesFromPackages = loadClassesFromPackages(directoriesToScan);
        List<Class<?>> annotatedComponents = findAnnotatedComponents(loadClassesFromPackages);
        return createBeans(annotatedComponents);
    }

    private List<Class<?>> loadClassesFromPackages(List<String> directoriesToScan) {
        BeanClassLoader classLoader = new BeanClassLoader();
        return classLoader.loadClassesFromPackages(directoriesToScan);
    }


    private List<Class<?>> findAnnotatedComponents(List<Class<?>> loadedClasses) {
        List<Class<?>> componentClasses = new LinkedList<>();
        for (Class<?> clazz : loadedClasses) {
            if (isAnnotatedComponent(clazz)) {
                componentClasses.add(clazz);
            }
        }
        return componentClasses;
    }

    private boolean isAnnotatedComponent(Class<?> clazz) {
        for (Annotation annotation: clazz.getDeclaredAnnotations()) {
            if (isComponentAnnotation(annotation)) {
                return true;
            }
        }
        return false;
    }

    private BeanCategory getBeanCategoryAccordingToAnnotation(Annotation annotation) {
        IBeanCategoryHandler handler = classToCategoryMap.get(annotation.annotationType());
        return handler.getBeanCategory();
    }

    private Annotation getComponentAnnotation(Class<?> clazz) {
        for (Annotation annotation: clazz.getDeclaredAnnotations()) {
            if (isComponentAnnotation(annotation)) {
                return annotation;
            }
        }
        return null;
    }

    private boolean isComponentAnnotation(Annotation annotation) {
        return componentAnnotations.contains(annotation.annotationType());
    }

    private List<Bean> createBeans(List<Class<?>> annotatedClasses) {
        List<Bean> beans = new ArrayList<>(annotatedClasses.size());
        for (Class<?> clazz : annotatedClasses) {
            Bean newBean = createBeanFromAnnotatedClass(clazz);
            beans.add(newBean);
        }
        return beans;
    }

    private Bean createBeanFromAnnotatedClass(Class<?> clazz) {
        Annotation annotation = getComponentAnnotation(clazz);
        String beanName = getBeanName(annotation.annotationType(), clazz);
        BeanCategory beanCategory = getBeanCategoryAccordingToAnnotation(annotation);
        return new Bean(beanName, clazz, beanCategory);
    }

    private String getBeanName(Class<?> annotationType, Class<?> clazz) {
        return classToCategoryMap.get(annotationType).getBeanName(clazz);
    }

}
