package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.view.api.FieldComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Doublebox;

import java.lang.reflect.Field;

@org.springframework.stereotype.Component
public class DoubleField implements FieldComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{double.class, Double.class, float.class, Float.class};
    }

    @Override
    public Component getComponent(Field e) {
        return new Doublebox();
    }

}
