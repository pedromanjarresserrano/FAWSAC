package com.gitlab.pedrioko.core.view.api;

import org.zkoss.zk.ui.Component;

public interface MenuProvider {

    String getLabel();

    Component getView();

    String getIcon();

    int getPosition();

    String getGroup();

    default boolean isOpenByDefault() {
        return false;
    }

}
