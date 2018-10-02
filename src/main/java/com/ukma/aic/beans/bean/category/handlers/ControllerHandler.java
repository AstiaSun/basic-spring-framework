package com.ukma.aic.beans.bean.category.handlers;

import com.ukma.aic.annotations.Controller;
import com.ukma.aic.beans.BeanCategory;

public class ControllerHandler implements IBeanCategoryHandler {
    @Override
    public String getBeanName(Class<?> clazz) {
        return clazz.getDeclaredAnnotation(Controller.class).name();
    }

    @Override
    public BeanCategory getBeanCategory() {
        return BeanCategory.CONTROLLER;
    }
}
