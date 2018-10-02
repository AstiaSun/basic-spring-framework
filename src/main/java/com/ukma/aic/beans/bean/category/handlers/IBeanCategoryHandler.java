package com.ukma.aic.beans.bean.category.handlers;

import com.ukma.aic.beans.BeanCategory;

public interface IBeanCategoryHandler {

    String getBeanName(Class<?> clazz);

    BeanCategory getBeanCategory();
}
