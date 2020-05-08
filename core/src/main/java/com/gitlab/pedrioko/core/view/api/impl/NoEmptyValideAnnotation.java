package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.NoEmpty;
import com.gitlab.pedrioko.core.view.api.ValidateAnnotation;
import com.gitlab.pedrioko.core.view.exception.ValidationException;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.StringUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;

@Component
public class NoEmptyValideAnnotation implements ValidateAnnotation {

    @Override
    public void Validate(Field f, Object value) {
        if (f.isAnnotationPresent(NoEmpty.class) && (value == null || (value instanceof String && ((String) value).isEmpty()) || (value instanceof Collection && ((Collection) value).isEmpty())))
            throw new ValidationException(ReflectionZKUtil.getLabel(f) + " "
                    + StringUtil.getDescapitalize(ReflectionZKUtil.getLabel("novacio")));
    }

}
