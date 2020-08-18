package com.gitlab.pedrioko.core.view.api;

import com.gitlab.pedrioko.core.lang.Page;
import org.zkoss.zk.ui.Component;

public interface MenuProvider {

    String getLabel();

    Page getView();

    String getIcon();

    int getPosition();

    String getGroup();

    default boolean isOpenByDefault() {
        return false;
    }

}
