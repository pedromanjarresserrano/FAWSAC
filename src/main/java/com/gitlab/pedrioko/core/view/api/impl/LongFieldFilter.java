package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.FieldFilter;
import com.gitlab.pedrioko.core.view.api.FieldFilterComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Longbox;

import java.lang.reflect.Field;

@FieldFilter
public class LongFieldFilter implements FieldFilterComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{long.class, Long.class};
    }

    @Override
    public Component getComponent(Field field) {
        return new Longbox();
    }

}
