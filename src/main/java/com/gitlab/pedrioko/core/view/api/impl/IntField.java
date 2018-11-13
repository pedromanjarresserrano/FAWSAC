package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.FieldForm;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import com.gitlab.pedrioko.core.view.forms.EntityForm;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Intbox;

import java.lang.reflect.Field;

@FieldForm
public class IntField implements FieldComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{short.class, Short.class, int.class, Integer.class};
    }

    @Override
    public Component getComponent(Field e, EntityForm f) {
        return new Intbox();
    }

}
