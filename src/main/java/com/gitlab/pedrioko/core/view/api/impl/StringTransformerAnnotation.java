package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.view.api.TransformerAnnotation;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class StringTransformerAnnotation implements TransformerAnnotation {

    @Override
    public Object Validate(Field f, Object value) {
        if (value instanceof String)
            return ((String) value).trim();
        return value;
    }

}
