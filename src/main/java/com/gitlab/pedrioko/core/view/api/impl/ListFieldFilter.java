package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.FieldFilter;
import com.gitlab.pedrioko.core.lang.annotation.UseChosenFileEntity;
import com.gitlab.pedrioko.core.view.api.FieldFilterComponent;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.zk.component.ChosenFileEntityBox;
import com.gitlab.pedrioko.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

import static com.gitlab.pedrioko.core.view.util.ApplicationContextUtils.getEntities;

@FieldFilter
public class ListFieldFilter implements FieldFilterComponent {
    @Autowired
    private CrudService crudService;

    @Override
    public Class[] getToClass() {
        return new Class[]{List.class};
    }

    @Override
    public Component getComponent(Field field) {
        Class<?> klass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        if (klass.isEnum()) {
            Combobox combobox = new Combobox();
            ReflectionZKUtil.populate(combobox, Arrays.asList(klass.getEnumConstants()), false);
            return combobox;
        } else {

            if (getEntities().contains(klass)) {
                if (field.isAnnotationPresent(UseChosenFileEntity.class)) {
                    ChosenFileEntityBox chosenFileEntityBox = new ChosenFileEntityBox();
                    chosenFileEntityBox.setModel(crudService.getAll(klass));
                    return chosenFileEntityBox;
                } else {
                    Combobox combobox = new Combobox();
                    ReflectionZKUtil.populate(combobox, crudService.getAll(klass), false);
                    return combobox;
                }
            }
        }

    
        return null;
    }

}
