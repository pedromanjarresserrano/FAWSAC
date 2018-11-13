package com.gitlab.pedrioko.core.view.api;

import com.gitlab.pedrioko.core.view.forms.EntityForm;
import org.zkoss.zk.ui.Component;

import java.lang.reflect.Field;

public interface FieldComponent {

    public Class[] getToClass();

    public Component getComponent(Field e, EntityForm f);
}
