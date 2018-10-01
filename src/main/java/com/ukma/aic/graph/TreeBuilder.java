package com.ukma.aic.graph;

import com.ukma.aic.beans.Bean;
import com.ukma.aic.beans.DependencyLoader;

import java.util.*;
import java.util.stream.Collectors;

public class TreeBuilder {
    private DependencyGraph<Node> dependencyGraph;
    private List<Boolean> visited;

    private static TreeBuilder treeBuilder = new TreeBuilder();

    private TreeBuilder() {

    }

    public static TreeBuilder getInstance() {
        return treeBuilder;
    }

    public void buildDependencyGraph(HashMap<String, Bean> beans) {
        initializeDependencyGraph(beans);
        DependencyLoader dependencyLoader = new DependencyLoader(beans);
        for (String beanName : beans.keySet()) {
            List<Bean> dependencies = dependencyLoader.getDependencies(beans.get(beanName));
            addDependenciesToNode(beanName, dependencies);
        }
    }

    private void initializeDependencyGraph(HashMap<String, Bean> beans) {
        dependencyGraph = new DependencyGraph<>();
        for (String beanName: beans.keySet()) {
            dependencyGraph.addFirst(new Node(beanName));
        }
    }

    private void addDependenciesToNode(String beanName, List<Bean> dependencies) {
        Node node = dependencyGraph.getNodeByName(beanName);
        for (Bean dependentBean: dependencies) {
            Node dependentNode = dependencyGraph.getNodeByName(dependentBean.getName());
            node.getDependencies().add(dependentNode);
        }
    }

    public boolean hasCycle() {
        List<Boolean> booleans = Collections.nCopies(dependencyGraph.size(), false);
        visited = new ArrayList<>(booleans);
        Node root = dependencyGraph.getFirst();
        return hasCycle(root);
    }

    private boolean hasCycle(Node node) {
        int currentIndex = dependencyGraph.indexOf(node);
        if (visited.get(currentIndex)) {
            return true;
        } else {
            visited.set(currentIndex, true);
        }
        for (Node dependentNode : node.getDependencies()) {
            if (hasCycle(dependentNode)) {
                return true;
            }
        }
        return false;
    }

    private Node findLeafAndDeleteFromGraph(Node currentNode) {
        List<Node> dependencies = currentNode.getDependencies();
        if ((dependencies.size() == 0)) {
            return removeNodeFromGraph(currentNode);
        } else {
            return findLeafAndDeleteFromGraph(currentNode.getDependencies().get(0));
        }

    }

    private Node removeNodeFromGraph(Node currentNode) {
        for (Node node : dependencyGraph) {
            node.getDependencies().remove(currentNode);
        }
        dependencyGraph.remove(currentNode);
        return currentNode;
    }

    public Queue<String> buildBeanCreationQueue() {
        Queue<String> beanQueue = new PriorityQueue<>();
        DependencyGraph<Node> savedDependencyGraph = new DependencyGraph<>(dependencyGraph);
        while (dependencyGraph.size() > 0) {
            Node leaf = findLeafAndDeleteFromGraph(dependencyGraph.getFirst());
            beanQueue.add(leaf.getBeanName());
        }
        dependencyGraph = savedDependencyGraph;
        return beanQueue;
    }

    public List<String> getDependentBeanNames(String beanName) {
        List<Node> dependencies = dependencyGraph.getNodeByName(beanName).getDependencies();
        return dependencies.stream()
                .map(Node::getBeanName)
                .collect(Collectors.toList());
    }
}