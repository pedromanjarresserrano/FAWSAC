package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.FieldForm;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Longbox;

import java.lang.reflect.Field;

@org.springframework.stereotype.Component
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
