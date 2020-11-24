package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.FieldFilter;
import com.gitlab.pedrioko.core.view.api.FieldFilterComponent;
import com.gitlab.pedrioko.core.zk.component.rangebox.DateRangeBox;
import org.zkoss.zk.ui.Component;

import java.lang.reflect.Field;
import java.util.Date;

@org.springframework.stereotype.Component
public class DateFieldFilter implements FieldFilterComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{Date.class};
    }

    @Override
    public Component getComponent(Field e) {
        return new DateRangeBox();
    }

}
