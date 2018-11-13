package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.FieldFilter;
import com.gitlab.pedrioko.core.view.api.FieldFilterComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Doublebox;

import java.lang.reflect.Field;

@FieldFilter
public class DoubleFieldFilter implements FieldFilterComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{double.class, Double.class, float.class, Float.class};
    }

    @Override
    public Component getComponent(Field e) {
        return new Doublebox();
    }

}
