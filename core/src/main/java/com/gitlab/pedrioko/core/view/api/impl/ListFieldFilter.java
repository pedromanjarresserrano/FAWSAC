package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.FieldFilter;
import com.gitlab.pedrioko.core.lang.annotation.UseChosenFileEntity;
import com.gitlab.pedrioko.core.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.ChosenItem;
import com.gitlab.pedrioko.core.view.api.FieldFilterComponent;
import com.gitlab.pedrioko.core.zk.component.chosenbox.ChosenBoxImage;
import com.gitlab.pedrioko.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

import static com.gitlab.pedrioko.core.view.util.ApplicationContextUtils.getEntities;

@FieldFilter
public class ListFieldFilter implements FieldFilterComponent {
    @Autowired
    private CrudService crudService;

    @Override
    public Class[] getToClass() {
        return new Class[]{List.class, Collections.class, Set.class};
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
                    String orderBy = field.getAnnotation(UseChosenFileEntity.class).orderBy();
                    ChosenBoxImage chosenFileEntityBox = new ChosenBoxImage();
                    List<?> allOrderBy = crudService.getAllOrderBy(klass, orderBy);
                    Map<Object, ChosenItem> model = allOrderBy.stream().collect(Collectors.toMap(ReflectionJavaUtil::getIdValue, x -> (ChosenItem) x, (a, b) -> b, LinkedHashMap::new));
                    chosenFileEntityBox.setModel(model);
                    return chosenFileEntityBox;
                } else {
                    Combobox combobox = new Combobox();
                    ReflectionZKUtil.populate(combobox, crudService.getAllOrder(klass), false);
                    return combobox;
                }
            }
        }


        return null;
    }

}
