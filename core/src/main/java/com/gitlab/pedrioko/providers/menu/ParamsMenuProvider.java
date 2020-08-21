package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.AppParam;
import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;

@Menu
public class ParamsMenuProvider implements MenuProvider {

    Page page = new Page(AppParam.class);

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("Params");
    }

    @Override
    public Page getView() {
        return this.page;
    }

    @Override
    public String getIcon() {
        return "fa fa-users";
    }

    @Override
    public int getPosition() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Class<?> getGroup() {
        return "administracion";
    }
}