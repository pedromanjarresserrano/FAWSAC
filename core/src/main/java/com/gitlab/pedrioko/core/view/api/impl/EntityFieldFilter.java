package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.FieldFilter;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.FieldFilterComponent;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.services.CrudService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;

import java.lang.reflect.Field;

@FieldFilter
public class EntityFieldFilter implements FieldFilterComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{};
    }

    @Override
    public Component getComponent(Field e) {
        Class<?> type = e.getType();
        if (ApplicationContextUtils.getEntities().contains(type)) {
            Combobox combobox = new Combobox();
            ReflectionZKUtil.populate(combobox, ApplicationContextUtils.getBean(CrudService.class).getAll(type), false);
            return combobox;
        } else {
            return null;
        }
    }

}
