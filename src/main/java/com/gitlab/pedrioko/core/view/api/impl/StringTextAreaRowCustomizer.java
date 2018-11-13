package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.Customizer;
import com.gitlab.pedrioko.core.lang.annotation.TextArea;
import com.gitlab.pedrioko.core.view.api.RowCustomizer;
import org.zkoss.zul.Div;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Customizer
public class StringTextAreaRowCustomizer implements RowCustomizer {

    @Override
    public List<?> getToClass() {
        return Arrays.asList(String.class);
    }

    @Override
    public void customizer(Div div, Field f) {
        if (f != null && f.isAnnotationPresent(TextArea.class)) {
            div.setZclass("col-sm-10 col-md-10 col-lg-10");
            ((Div) div.getChildren().get(0)).setZclass("col-md-2");
            ((Div) div.getChildren().get(1)).setZclass("col-md-10");
        }
    }

}
