package com.ukma.aic.beans;

import com.ukma.aic.annotations.Autowired;
import com.ukma.aic.exceptions.BeanNotFoundException;
import com.ukma.aic.exceptions.DependencyCycleFoundException;
import com.ukma.aic.graph.TreeBuilder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.ukma.aic.utils.Utils.getClassByClassPath;

public class BeanContext {
    private HashMap<String, Bean> beans;
    private List<String> beanCreationQueue;

    /**
     * When bean context is created, firstly, all beans from configuration file are loaded into the list.
     * Secondly, dependencies between beans are found and registered. Dependencies are found using
     * @see Autowired @Autowired annotation. Next, dependency tree is built.
     * If cycle is found in the tree, an exception is thrown an bean context will not be created.
     * Otherwise, the sequence of bean's instance creations will bw built in order to inject dependencies.
     * When all beans will be initialized, the bean context will be finally created.
     *
     * @param configurationFile name of xml file where beans are declared. Must be located in resource directory
     * @throws DependencyCycleFoundException thrown if cycle is found in dependency tree
     */
    public BeanContext(String configurationFile) throws DependencyCycleFoundException {
        beans = new LinkedHashMap<>();
        loadBeanDescriptionsFromConfigurationFile(configurationFile);
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

    private void loadBeanDescriptionsFromConfigurationFile(String configurationFile) {
        try {
            ConfigurationParser configurationParser = new ConfigurationParser(configurationFile);
            createBeansFromBeanDescriptions(configurationParser);
            createBeansFromContainers(configurationParser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createBeansFromBeanDescriptions(ConfigurationParser configurationParser){
        List<String> ids = configurationParser.getBeanIds();
        List<String> classes = configurationParser.getBeanClasses();
        createBeanDescriptionsIfListSizesAreEqual(ids, classes);
        HashMap<String, BeanScope> scopesByIds = configurationParser.getScopesByIds(ids);
        setBeanScopes(scopesByIds);
    }

    private void createBeansFromContainers(ConfigurationParser configurationParser) {
        List<String> componentContainers = configurationParser.getComponentContainers();
        ComponentScanner componentScanner = new ComponentScanner();
        List<Bean> componentBeans = componentScanner.scanForAnnotatedBeans(componentContainers);
        componentBeans.forEach(bean -> beans.put(bean.getName(), bean));
    }

    private void createBeanDescriptions(List<String> ids, List<String> classes) {
        for (int i = 0; i < ids.size(); i++) {
            Class<?> aClass = getClassByClassPath(classes.get(i));
            Bean bean = new Bean(ids.get(i), aClass);
            beans.put(bean.getName(), bean);
        }
    }

    private void createBeanDescriptionsIfListSizesAreEqual(List<String> ids, List<String> classes) {
        try {
            checkIfListSizeAreEqual(ids, classes);
            createBeanDescriptions(ids, classes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBeanScopes(HashMap<String, BeanScope> scopes) {
        for (String key: scopes.keySet()) {
            beans.get(key).setBeanScope(scopes.get(key));
        }
    }


    private void checkIfListSizeAreEqual(List<?> ids, List<?> classes) throws Exception {
        if (ids.size() != classes.size()) {
            throw new Exception("com.ukma.aic.beans.Bean's id or class is not specified in configuration file");
        }
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
        if (!beans.containsKey(beanName))
            return null;
        return getObjectFromBean(beans.get(beanName));
    }

    private Object getObjectFromBean(Bean bean) {
        if (bean.getBeanScope() == BeanScope.SINGLETON) {
            return bean.getBean();
        } else if (bean.getBeanScope() == BeanScope.PROTOTYPE) {
            InstanceCreator instanceCreator = new InstanceCreator(beans);
            return createBeanInstanceWithDependencies(instanceCreator, bean.getName());
        }
        throw new NullPointerException("Bean scope is not set for bean with id=" + bean.getName());
    }
}
