package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.FieldForm;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import com.gitlab.pedrioko.core.view.forms.EntityForm;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Doublebox;

import java.lang.reflect.Field;

@FieldForm
public class DoubleField implements FieldComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{double.class, Double.class, float.class, Float.class};
    }

    @Override
    public Component getComponent(Field e, EntityForm f) {
        return new Doublebox();
    }

}
