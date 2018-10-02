package com.ukma.aic.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class BeanClassLoader {
    public List<Class<?>> loadClassesFromPackages(List<String> packagesToScan) {
        List<Class<?>> loadedClasses = new LinkedList<>();
        for (String packagePath : packagesToScan) {
            loadedClasses.addAll(loadClassesFromPackage(packagePath));
        }
        return loadedClasses;
    }

    private List<Class<?>> loadClassesFromPackage(String packagePath) {
        try {
            return loadClassesFromPackageOrThrowException(packagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedList<>();
    }

    private List<Class<?>> loadClassesFromPackageOrThrowException(String packageName) throws IOException {
        packageName = packageName.replace(".", "/");
        java.lang.ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(packageName);
        List<Class<?>> loadedClasses = new LinkedList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File packageFile = new File(resource.getFile());
            loadedClasses.addAll(getClassesInPackage(packageFile, packageName));
        }
        return loadedClasses;
    }

    private List<Class<?>> getClassesInPackage(File directory, String packageName) {
        if (!directory.exists()) {
            return new ArrayList<>();
        }
        List<Class<?>> classes = new LinkedList<>();
        for (File file : directory.listFiles()) {
            if (!file.isDirectory() && (file.getName().endsWith(".class"))) {
                String className = file.getName().replace(".class", "");
                Class loadedClass = loadClass(packageName + "." + className);
                if (loadedClass != null) {
                    classes.add(loadedClass);
                }
            }
        }
        return classes;
    }

    private Class loadClass(String classReference) {
        try {
            return Class.forName(classReference);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
