package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.CapitalizeFully;
import com.gitlab.pedrioko.core.view.api.TransformerAnnotation;
import org.apache.commons.lang.WordUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class StringTransformerAnnotation implements TransformerAnnotation {

    @Override
    public Object Validate(Field f, Object value) {
        if (value instanceof String) {
            String trim = ((String) value);
            trim = ((String) value).trim();

            if (f.getAnnotation(CapitalizeFully.class) != null) {
                trim = WordUtils.capitalizeFully(trim);
            }
            return trim;
        }
        return value;
    }

}
