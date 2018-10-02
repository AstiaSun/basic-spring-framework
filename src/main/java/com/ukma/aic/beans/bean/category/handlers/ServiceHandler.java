package com.ukma.aic.beans.bean.category.handlers;

import com.ukma.aic.annotations.Service;
import com.ukma.aic.beans.BeanCategory;

public class ServiceHandler implements IBeanCategoryHandler {
    @Override
    public String getBeanName(Class<?> clazz) {
        return clazz.getDeclaredAnnotation(Service.class).name();
    }

    @Override
    public BeanCategory getBeanCategory() {
        return BeanCategory.SERVICE;
    }
}
