package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.Email;
import com.gitlab.pedrioko.core.view.api.ValidateAnnotation;
import com.gitlab.pedrioko.core.view.exception.ValidationException;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.StringUtil;
import com.gitlab.pedrioko.core.view.util.TextValidator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class EmailValideAnnotation implements ValidateAnnotation {

    @Override
    public void Validate(Field f, Object value) {
        if (f.isAnnotationPresent(Email.class)) {
            if ((value == null || ((String) value).isEmpty()
                    || !TextValidator.validateEmail((String) value)))
                throw new ValidationException(ReflectionZKUtil.getLabel(f) + " "
                        + StringUtil.getDescapitalize(ReflectionZKUtil.getLabel("noemail")));
        }
    }

}
