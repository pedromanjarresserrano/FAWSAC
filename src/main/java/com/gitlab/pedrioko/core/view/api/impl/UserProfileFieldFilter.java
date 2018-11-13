package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.UserProfile;
import com.gitlab.pedrioko.core.lang.annotation.FieldFilter;
import com.gitlab.pedrioko.core.view.api.FieldFilterComponent;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@FieldFilter
public class UserProfileFieldFilter implements FieldFilterComponent {
    @Autowired
    private CrudService crudService;

    @Override
    public Class[] getToClass() {
        return new Class[]{List.class};
    }

    @Override
    public Component getComponent(Field field) {
        Class<?> klass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        if (klass == UserProfile.class) {
            Combobox combobox = new Combobox();
            ReflectionZKUtil.populate(combobox, crudService.getAll(UserProfile.class), false);
            return combobox;
        }
        return null;
    }

}
