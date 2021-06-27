package org.example.redisdemo.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @desc 描述
 * @author xsir
 * @date 2021/6/27 下午10:45
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    /**
     * Spring应用上下文环境
     */
    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的回调方法。设置上下文环境
     *
     * @param applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取对象
     *
     * @param name
     * @return Object
     * @throw BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 获取对象通过Class
     *
     * @param cls
     * @return Object
     * @throws BeansException
     */
    public static <C> Object getBean(Class<C> cls) throws BeansException {
        return applicationContext.getBean(cls);
    }
}