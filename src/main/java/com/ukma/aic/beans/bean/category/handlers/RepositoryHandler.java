package com.ukma.aic.beans.bean.category.handlers;

import com.ukma.aic.annotations.Repository;
import com.ukma.aic.beans.BeanCategory;

public class RepositoryHandler implements IBeanCategoryHandler {
    @Override
    public String getBeanName(Class<?> clazz) {
        return clazz.getDeclaredAnnotation(Repository.class).name();
    }

    @Override
    public BeanCategory getBeanCategory() {
        return BeanCategory.REPOSITORY;
    }
}
