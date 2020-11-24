package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.Email;
import com.gitlab.pedrioko.core.lang.annotation.FieldForm;
import com.gitlab.pedrioko.core.lang.annotation.Password;
import com.gitlab.pedrioko.core.lang.annotation.TextArea;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Textbox;

import java.lang.reflect.Field;

@org.springframework.stereotype.Component
public class StringField implements FieldComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{String.class, char.class};
    }

    @Override
    public Component getComponent(Field field) {
        Textbox textbox = new Textbox();
        if (field.isAnnotationPresent(Password.class))
            textbox.setType("password");
        if (field.isAnnotationPresent(Email.class)) {
            textbox.setType("email");
        }
        if (field.isAnnotationPresent(TextArea.class)) {
            textbox.setMultiline(true);
            textbox.setHeight("80px");
            textbox.setWidth("100%");
        }
        return textbox;
    }

}
