package com.gitlab.pedrioko.core.view.api;

import com.gitlab.pedrioko.core.lang.Page;

public interface MenuProvider extends Provider {

    Page getView();

    Class<? extends Provider> getGroup();

    default boolean isOpenByDefault() {
        return false;
    }

}
