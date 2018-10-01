package com.ukma.aic.graph;

import java.util.LinkedList;

public class DependencyGraph<T> extends LinkedList<T> {
    DependencyGraph() {
        super();
    }

    DependencyGraph(DependencyGraph<T> dependencyGraph) {
        super(dependencyGraph);
    }

    Node getNodeByName(String beanName) {
        for (T t : this) {
            Node node = (Node) t;
            if (node.getBeanName().equals(beanName)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public boolean contains(Object o) {
        String beanName = ((Node) o).getBeanName();
        while (iterator().hasNext()) {
            Node nextNode = (Node) iterator().next();
            if (nextNode.getBeanName().equals(beanName)) {
                return true;
            }
        }
        return false;
    }
}
