package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.Password;
import com.gitlab.pedrioko.core.view.api.TransformerAnnotation;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

@Component
public class PasswordTransformerAnnotation implements TransformerAnnotation {
    private Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

    @Override
    public Object Validate(Field f, Object value) {
        if (f.isAnnotationPresent(Password.class) && !isValid(value.toString())) {
            String encode = ApplicationContextUtils.getBean(PasswordEncoder.class).encode(value.toString());
            return encode;
        }
        return value;
    }

    public boolean isValid(String s) {
        return BCRYPT_PATTERN.matcher(s).matches();
    }
}
