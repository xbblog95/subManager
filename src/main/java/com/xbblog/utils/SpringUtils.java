package com.xbblog.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public final class SpringUtils implements ApplicationContextAware {

    //静态
    private static ApplicationContext APPLICATIONCONTEXT; // Spring应用上下文环境

    public static Object getBean(String name) throws BeansException {
        return APPLICATIONCONTEXT.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) throws BeansException
    {
        return APPLICATIONCONTEXT.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATIONCONTEXT = applicationContext;
    }
}
