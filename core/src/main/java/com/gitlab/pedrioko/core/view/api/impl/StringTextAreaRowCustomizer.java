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

        }
    }

}
