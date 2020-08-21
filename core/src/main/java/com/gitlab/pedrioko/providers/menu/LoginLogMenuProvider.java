package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.api.Provider;
import com.gitlab.pedrioko.domain.LoginLog;

@Menu
public class LoginLogMenuProvider implements MenuProvider {

    Page page = new Page(LoginLog.class);

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("LoginLog");
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
        return Integer.MAX_VALUE - 2;
    }

    @Override
    public Class<? extends Provider> getGroup() {
        return AdminGroupProvider.class;
    }
}