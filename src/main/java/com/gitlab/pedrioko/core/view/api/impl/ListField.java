package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.FieldForm;
import com.gitlab.pedrioko.core.lang.annotation.Reference;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import com.gitlab.pedrioko.core.view.enums.CrudMode;
import com.gitlab.pedrioko.core.view.forms.EntityForm;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.PropertiesUtil;
import com.gitlab.pedrioko.core.view.viewers.CrudView;
import com.gitlab.pedrioko.core.zk.component.ChosenBox;
import com.gitlab.pedrioko.core.zk.component.StringListBox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Tab;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

@FieldForm
public class ListField implements FieldComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{List.class};
    }

    @Override
    public Component getComponent(Field e, EntityForm ef) {
        Class<?> klass = (Class<?>) ((ParameterizedType) e.getGenericType()).getActualTypeArguments()[0];
        if (e.isAnnotationPresent(Reference.class)) {
            Class<?> value = e.getAnnotation(Reference.class).value();
            return getComponent(e, ef, value);
        } else {
            if (klass.isEnum()) {
                ChosenBox chosenbox = new ChosenBox();
                chosenbox.setModel(Arrays.asList(klass.getEnumConstants()));
                return chosenbox;
            } else {
                if (klass == String.class) {
                    return new StringListBox(ReflectionZKUtil.getLabel(e.getName()));
                } else return getComponent(e, ef, klass);
            }
        }

    }

    public Component getComponent(Field e, EntityForm ef, Class<?> klass) {
        ef.getTabs().appendChild(new Tab((ReflectionZKUtil.getLabel(e))));
        CrudView crudView = new CrudView(klass, CrudMode.SUBCRUD);
        PropertiesUtil propertiesUtil = ApplicationContextUtils.getBean(PropertiesUtil.class);
        boolean enableSubCrudsClass = propertiesUtil
                .getEnableSubCrudsClassProperty(klass, e.getName(), true);
        crudView.enableCommonCrudActions(enableSubCrudsClass);
        crudView.setStyle("height:100%;");
        crudView.setReloadable(false);
        ef.getTabpanels().appendChild(crudView);
        ef.putBinding(e, crudView);
        return null;
    }
}
