package com.ukma.aic.beans;

import com.ukma.aic.exceptions.BeanNotFoundException;
import com.ukma.aic.exceptions.DependencyCycleFoundException;
import com.ukma.aic.graph.TreeBuilder;
import org.apache.commons.configuration.XMLConfiguration;

import java.util.*;

import static com.ukma.aic.utils.Utils.getClassByClassPath;

public class BeanContext {
    private HashMap<String, Bean> beans;
    private XMLConfiguration configuration;
    private List<String> beanCreationQueue;

    /**
     * When bean context is created, firstly, all beans from configuration file are loaded into the list.
     * Secondly, dependencies between beans are found and registered. Dependencies are found using
     * @see com.ukma.aic.annotation.Autowired @Autowired annotation. Next, dependency tree is built.
     * If cycle is found in the tree, an exception is thrown an bean context will not be created.
     * Otherwise, the sequence of bean's instance creations will bw built in order to inject dependencies.
     * When all beans will be initialized, the bean context will be finally created.
     *
     * @param configurationFile name of xml file where beans are declared. Must be located in resource directory
     * @throws DependencyCycleFoundException thrown if cycle is found in dependency tree
     */
    public BeanContext(String configurationFile) throws DependencyCycleFoundException {
        String pathToConfigurationFile = getConfigurationFile(configurationFile);
        configuration = new XMLConfiguration();
        configuration.setFileName(pathToConfigurationFile);
        loadBeanDescriptionsFromConfigurationFile();
        checkCyclesAndBuildBeanCreationQueue();
        createBeanInstances();
    }

    private void checkCyclesAndBuildBeanCreationQueue() throws DependencyCycleFoundException {
        TreeBuilder treeBuilder = TreeBuilder.getInstance();
        treeBuilder.buildDependencyGraph(beans);
        if (treeBuilder.hasCycle()) {
            throw new DependencyCycleFoundException();
        }
        beanCreationQueue = treeBuilder.buildBeanCreationQueue();
    }

    private void loadBeanDescriptionsFromConfigurationFile() {
        try {
            configuration.load();
            readBeanDescriptionsFromConfigurationFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readBeanDescriptionsFromConfigurationFile() throws Exception {
        List<?> ids = getPropertiesFromConfiguration("bean[@id]");
        List<?> classes = getPropertiesFromConfiguration("bean[@class]");
        checkIfListSizeAreEqual(ids, classes);
        createBeanDescriptions(ids, classes);
    }

    private void createBeanDescriptions(List<?> ids, List<?> classes) {
        beans = new LinkedHashMap<>();
        for (int i = 0; i < ids.size(); i++) {
            Class<?> aClass = getClassByClassPath((String) classes.get(i));
            Bean bean = new Bean((String) ids.get(i), aClass);
            beans.put(bean.getName(), bean);
        }
    }


    private void checkIfListSizeAreEqual(List<?> ids, List<?> classes) throws Exception {
        if (ids.size() != classes.size()) {
            throw new Exception("com.ukma.aic.beans.Bean's id or class is not specified in configuration file");
        }
    }

    private List<?> getPropertiesFromConfiguration(String xpath) {
        Object property = configuration.getProperty(xpath);
        if (property instanceof Collection) {
            return (List<?>)property;
        }
        LinkedList<Object> properties = new LinkedList<>();
        properties.add(property);
        return properties;
    }


    private String getConfigurationFile(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResource(filename).getFile();
    }

    private void createBeanInstances() {
        InstanceCreator instanceCreator = new InstanceCreator(beans);
        for (String beanName : beanCreationQueue) {
            Object object = createBeanInstanceWithDependencies(instanceCreator, beanName);
            beans.get(beanName).setBean(object);
        }
    }

    private Object createBeanInstanceWithDependencies(InstanceCreator instanceCreator, String beanName) {
        try {
            return instanceCreator.createObject(beans.get(beanName));
        } catch (BeanNotFoundException e) {
            e.printStackTrace();
        }
        return new Object();
    }

    public Object loadObject(String beanName) {
        return beans.get(beanName).getBean();
    }
}
