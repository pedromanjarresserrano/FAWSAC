package com.gitlab.pedrioko.core.view.api;

import org.zkoss.zk.ui.Component;

import java.util.List;

public interface ListCellCustomizer {

    List<Class<?>> getToClass();

    Component getCompònent(Object value);
}
