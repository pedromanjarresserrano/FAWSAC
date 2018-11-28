package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.UserProfile;
import com.gitlab.pedrioko.core.lang.annotation.FieldForm;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import com.gitlab.pedrioko.core.view.enums.CrudMode;
import com.gitlab.pedrioko.core.view.forms.EntityForm;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.PropertiesUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Tab;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Set;

@FieldForm
public class UserProfileField implements FieldComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{Set.class};
    }

    @Override
    public Component getComponent(Field e, EntityForm ef) {
        Class<?> klass = (Class<?>) ((ParameterizedType) e.getGenericType()).getActualTypeArguments()[0];
        if (klass == UserProfile.class) {
            ef.getTabs().appendChild(new Tab((ReflectionZKUtil.getLabel(e))));
            CrudView crudView = new CrudView(UserProfile.class, CrudMode.SUBCRUD);
            boolean enableSubCrudsClass = ApplicationContextUtils.getBean(PropertiesUtil.class)
                    .getEnableSubCrudsClass(UserProfile.class, false);
            crudView.enableCommonCrudActions(enableSubCrudsClass);
            crudView.setStyle("height:100%;");
            crudView.setReloadable(false);
            ef.getTabpanels().appendChild(crudView);
            ef.putBinding(e, crudView);
        }
        return null;
    }

}
