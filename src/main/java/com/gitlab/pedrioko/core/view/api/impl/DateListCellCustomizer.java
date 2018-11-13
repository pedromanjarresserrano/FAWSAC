package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.CellCustomizer;
import com.gitlab.pedrioko.core.view.api.ListCellCustomizer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@CellCustomizer
public class DateListCellCustomizer implements ListCellCustomizer {
    @Override
    public List<Class<?>> getToClass() {
        return Arrays.asList(Date.class);
    }

    @Override
    public Component getCompònent(Object value) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return new Label(simpleDateFormat.format(value));
        } catch (Exception e) {
            return new Label((String) value);
        }
    }
}
