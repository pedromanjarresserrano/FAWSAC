package com.gitlab.pedrioko.core.view.api;

import org.zkoss.zk.ui.Component;

import java.lang.reflect.Field;

public interface FieldFilterComponent {
    @SuppressWarnings("rawtypes")
    Class[] getToClass();

    Component getComponent(Field field);
}
