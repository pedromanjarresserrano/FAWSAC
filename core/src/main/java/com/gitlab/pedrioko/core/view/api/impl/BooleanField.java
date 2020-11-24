package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.FieldForm;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Checkbox;

import java.lang.reflect.Field;

@org.springframework.stereotype.Component
public class BooleanField implements FieldComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{boolean.class, Boolean.class};

    }

    @Override
    public Component getComponent(Field e) {
        return new Checkbox();
    }

}
