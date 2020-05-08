package com.gitlab.pedrioko.core.view.api;

import org.zkoss.zul.Div;

import java.lang.reflect.Field;
import java.util.List;

public interface RowCustomizer {

    public List<?> getToClass();

    public void customizer(Div div, Field f);
}
