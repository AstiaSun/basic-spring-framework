package com.ukma.aic.utils;

import java.lang.reflect.Method;

public class Utils {
    public static String LEFT_SECTION_MESSAGE = "\tLeft critical section...\t";
    public static String ENTERED_SECTION_MESSAGE = "\tInside critical section!\t";

    public static Method getSetterForField(Class aClass, String fieldName) {
        Method[] methods = aClass.getDeclaredMethods();
        fieldName = toUpperCaseFirstLetter(fieldName);
        for (Method method : methods) {
            if (method.getName().startsWith("set") && method.getName().endsWith(fieldName)) {
                return method;
            }
        }
        return null;
    }

    private static String toUpperCaseFirstLetter(String fieldName) {
        String firstLetter = fieldName.substring(0, 1);
        return fieldName.replaceFirst(firstLetter, firstLetter.toUpperCase());
    }

    public static Class<?> getClassByClassPath(String classPath) {
        try {
            return Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return Object.class;
    }

    public static Object newInstance(Class<?> aClass) {
        try {
            return aClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
