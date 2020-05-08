package com.gitlab.pedrioko.core.view.util;

import com.gitlab.pedrioko.core.lang.annotation.UIEntity;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Configuration
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext ctx;

    private static List<?> entities;

    public static List<?> getEntities() {
        if (entities == null) {
            entities = ApplicationContextUtils.getBean(EntityManagerFactory.class).getMetamodel().getEntities().stream()
                    .map(EntityType::getJavaType).filter(e -> !e.isAnnotationPresent(UIEntity.class))
                    .collect(Collectors.toList());
            return entities;
        } else
            return entities;
    }

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) {
        ctx = appContext;
    }

    public static List<String> getEventsListEntities() {
        List<String> entities = new ArrayList<>();
        getEntitiesNames().forEach(e -> {
            entities.add("Read" + e);
            entities.add("Save" + e);
            entities.add("Delete" + e);
        });
        return entities;
    }

    public static List<String> getEntitiesNames() {
        return getEntities().stream().map(Object::toString).map(e -> StringUtil.lastSplit(e, ".")).collect(Collectors.toList());
    }


    /**
     * @param arg0
     * @return
     * @throws BeansException
     * @see org.springframework.beans.factory.BeanFactory#getBean(java.lang.Class)
     */
    public static <T> T getBean(Class<T> arg0) {
        return ctx.getBean(arg0);
    }


    /**
     * @param arg0
     * @return
     * @throws BeansException
     * @see org.springframework.beans.factory.BeanFactory#getBean(java.lang.String)
     */
    public static Object getBean(String arg0) {
        return ctx.getBean(arg0);
    }


    /**
     * @param arg0
     * @return
     * @throws BeansException
     * @see org.springframework.beans.factory.ListableBeanFactory#getBeansOfType(java.lang.Class)
     */
    public static <T> List<T> getBeansOfType(Class<T> arg0) {
        return new ArrayList<>(ctx.getBeansOfType(arg0).values());
    }

    /**
     * @param arg0
     * @return
     * @throws BeansException
     * @see org.springframework.beans.factory.ListableBeanFactory#getBeansOfType(java.lang.Class)
     */
    public static <T> List<T> getBeans(Class<T> arg0) {
        return ctx.getBeansOfType(arg0).entrySet().stream().map(Entry<String, T>::getValue)
                .collect(Collectors.toList());
    }


}