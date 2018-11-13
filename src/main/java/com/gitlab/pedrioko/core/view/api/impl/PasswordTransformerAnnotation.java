package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.Password;
import com.gitlab.pedrioko.core.view.api.TransformerAnnotation;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.lang.reflect.Field;

@Component
public class PasswordTransformerAnnotation implements TransformerAnnotation {

    @Override
    public Object Validate(Field f, Object value) {
        if (f.isAnnotationPresent(Password.class) && !isValidMD5(value.toString()))
            return DigestUtils.md5DigestAsHex(value.toString().getBytes());
        return value;
    }

    public boolean isValidMD5(String s) {
        return s.matches("[a-fA-F0-9]{32}");
    }
}
