package com.ukma.aic.beans;

import com.ukma.aic.utils.ConfigurationParserConstants;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class ConfigurationParser {
    private XMLConfiguration configuration;

    ConfigurationParser(String configurationFile) throws ConfigurationException {
        String pathToConfigurationFile = getConfigurationFile(configurationFile);
        configuration = new XMLConfiguration();
        configuration.setFileName(pathToConfigurationFile);
        configuration.load();
    }

    private String getConfigurationFile(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResource(filename).getFile();
    }

    private List<?> getPropertiesFromConfiguration(String xpath) {
        Object property = configuration.getProperty(xpath);
        if (property instanceof Collection) {
            return (List<?>)property;
        } else if (property == null) {
            return new LinkedList<>();
        }
        LinkedList<Object> properties = new LinkedList<>();
        properties.add(property);
        return properties;
    }

    private List<?> getPropertiesFromConfiguration(ConfigurationParserConstants xpath) {
        return getPropertiesFromConfiguration(xpath.toString());
    }

    List<String> getBeanIds() {
        List<?> beanIds = getPropertiesFromConfiguration(ConfigurationParserConstants.beanIds);
        return objectsToString(beanIds);
    }

    List<String> getBeanClasses() {
        List<?> beanClasses = getPropertiesFromConfiguration(ConfigurationParserConstants.beanClasses);
        return objectsToString(beanClasses);
    }

    List<String> getComponentContainers() {
        List<?> containers = getPropertiesFromConfiguration(ConfigurationParserConstants.componentContainers);
        return objectsToString(containers);
    }

    private List<String> objectsToString(List<?> objects) {
        return objects.stream()
                .map(o -> (String) o)
                .collect(Collectors.toList());
    }

    HashMap<String, BeanScope> getScopesByIds(List<String> ids) {
        final String xpath = "bean(i)[@scope]";
        HashMap<String, BeanScope> resultScopes = new HashMap<>();
        for (Integer i = 0; i < ids.size(); i++) {
            Object property = configuration.getProperty(xpath.replace("i", i.toString()));
            if (property != null) {
                BeanScope beanScope = BeanScope.valueOf(String.valueOf(property).toUpperCase());
                resultScopes.put(ids.get(i), beanScope);
            }
        }
        return resultScopes;
    }
}
