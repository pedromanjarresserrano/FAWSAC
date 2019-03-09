package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.FieldForm;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import com.gitlab.pedrioko.core.view.forms.EntityForm;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Longbox;

import java.lang.reflect.Field;

@FieldForm
public class LongField implements FieldComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{long.class, Long.class};
    }

    @Override
    public Component getComponent(Field e) {
        return new Longbox();
    }

}
