package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.CellCustomizer;
import com.gitlab.pedrioko.core.view.api.ListCellCustomizer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Checkbox;

import java.util.Arrays;
import java.util.List;

@CellCustomizer
public class BooleanListCellCustomizer implements ListCellCustomizer {
    @Override
    public List<Class<?>> getToClass() {
        return Arrays.asList(Boolean.class, boolean.class);
    }

    @Override
    public Component getCompònent(Object value) {
        Checkbox checkbox = new Checkbox();
        checkbox.setDisabled(true);
        checkbox.setChecked(value != null ? (Boolean) value : false);
        return checkbox;
    }
}
