package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.view.api.FieldComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Datebox;

import java.lang.reflect.Field;
import java.util.Date;

@org.springframework.stereotype.Component
public class DateField implements FieldComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{Date.class};
    }

    @Override
    public Component getComponent(Field e) {
        return new Datebox();
    }

}
