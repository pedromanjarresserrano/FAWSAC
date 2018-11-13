package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.FieldForm;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import com.gitlab.pedrioko.core.view.forms.EntityForm;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Datebox;

import java.lang.reflect.Field;
import java.util.Date;

@FieldForm
public class DateField implements FieldComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{Date.class};
    }

    @Override
    public Component getComponent(Field e, EntityForm f) {
        return new Datebox();
    }

}
