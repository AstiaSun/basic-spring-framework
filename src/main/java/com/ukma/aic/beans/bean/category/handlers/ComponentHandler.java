package com.ukma.aic.beans.bean.category.handlers;

import com.ukma.aic.annotations.Component;
import com.ukma.aic.beans.BeanCategory;

public class ComponentHandler implements IBeanCategoryHandler {

    @Override
    public String getBeanName(Class<?> clazz) {
        return clazz.getDeclaredAnnotation(Component.class).name();
    }

    @Override
    public BeanCategory getBeanCategory() {
        return BeanCategory.COMPONENT;
    }
}
