package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.FieldForm;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import com.gitlab.pedrioko.core.view.api.FieldParamsComponent;
import com.gitlab.pedrioko.core.view.forms.EntityForm;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.services.CrudService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FieldForm
public class EntityField implements FieldComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{};
    }

    @Override
    public Component getComponent(Field e) {
        Class<?> declaringClass = e.getDeclaringClass();
        Class<?> type = e.getType();
        if (ApplicationContextUtils.getEntities().contains(type)) {
            Combobox combobox = new Combobox();
            CrudService crudService = ApplicationContextUtils.getBean(CrudService.class);
            PathBuilder<?> pathBuilder = crudService.getPathBuilder(type);
            Predicate where = null;
            List<FieldParamsComponent> beans = ApplicationContextUtils.getBeans(FieldParamsComponent.class).stream().filter(c -> c.getToClass().contains(declaringClass) && c.getFieldName().equalsIgnoreCase(e.getName())).collect(Collectors.toList());
            for (FieldParamsComponent m : beans) {
                for (Map.Entry<String, Object> v : m.getParam().entrySet()) {
                    if (where == null) where = pathBuilder.get(v.getKey()).eq(v.getValue());
                    else
                        where = pathBuilder.get(v.getKey()).eq(v.getValue()).and(where);
                }
            }
            List<?> all = where == null ? crudService.getAll(type) : crudService.query().from(pathBuilder).where(where).fetch();
            ReflectionZKUtil.populate(combobox, all, false);
            return combobox;
        } else {
            return null;
        }
    }

}
