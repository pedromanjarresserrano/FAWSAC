package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.view.api.FieldFilterComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Checkbox;

import java.lang.reflect.Field;

public class BooleanFieldFilter implements FieldFilterComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{boolean.class, Boolean.class};

    }

    @Override
    public Component getComponent(Field e) {
        return new Checkbox();
    }

}
