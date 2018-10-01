package com.ukma.aic.beans;

import org.apache.commons.configuration.XMLConfiguration;

import java.util.*;

import static com.ukma.aic.utils.Utils.getClassByClassPath;


public class DependencyConfiguration {
    private HashMap<String, Bean> beans;

    public DependencyConfiguration(String filename) {
        String pathToConfigurationFile = getConfigurationFile(filename);
        XMLConfiguration configuration = new XMLConfiguration();
        configuration.setFileName(pathToConfigurationFile);
        loadConfigurations(configuration);
    }

    private void loadConfigurations(XMLConfiguration configuration) {
        try {
            configuration.load();
            loadBeans(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBeans(XMLConfiguration configuration) throws Exception {
        List<?> ids = getPropertiesFromConfiguration("bean[@id]", configuration);
        List<?> classes = getPropertiesFromConfiguration("bean[@class]", configuration);
        checkIfListSizeAreEqual(ids, classes);
        loadBeans(ids, classes);
    }

    private void loadBeans(List<?> ids, List<?> classes) {
        beans = new LinkedHashMap<>();
        for (int i = 0; i < ids.size(); i++) {
            Bean bean = createBean((String) ids.get(i), (String) classes.get(i));
            beans.put(bean.getName(), bean);
        }
    }

    private Bean createBean(String id, String classReference) {
        Bean bean = new Bean(id, getClassByClassPath(classReference));
        DependencyLoader dependencyLoader = new DependencyLoader(beans);
        //Class clazz = getClassByClassPath(bean.getBeanType());
        //Object instance = dependencyLoader,
        //bean.setBean(instance);
        return bean;
    }

    private void checkIfListSizeAreEqual(List<?> array1, List<?> array2) throws Exception {
        if (array1.size() != array2.size()) {
            throw new Exception("com.ukma.aic.beans.Bean's id or class is not specified in configuration file");
        }
    }

    private List<?> getPropertiesFromConfiguration(String xpath, XMLConfiguration configuration) {
        Object property = configuration.getProperty(xpath);
        if (property instanceof Collection) {
            return (List<?>)property;
        }
        LinkedList<Object> properties = new LinkedList<>();
        properties.add(property);
        return properties;
    }

    public Object loadObject(String id) {
        Bean bean = beans.get(id);
        if (bean == null) {
            throw new NoSuchElementException("com.ukma.aic.beans.Bean with name='" + id + "' is not registered");
        }

        return bean.getBean();
    }


    private String getConfigurationFile(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResource(filename).getFile();
    }
}
