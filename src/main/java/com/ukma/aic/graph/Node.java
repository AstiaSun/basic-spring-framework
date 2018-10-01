package com.ukma.aic.graph;

import java.util.LinkedList;
import java.util.List;

class Node {
    private String beanName;
    private List<Node> dependencies;

    Node(String beanName) {
        this.beanName = beanName;
        dependencies = new LinkedList<>();
    }

    String getBeanName() {
        return beanName;
    }

    List<Node> getDependencies() {
        return dependencies;
    }
}
