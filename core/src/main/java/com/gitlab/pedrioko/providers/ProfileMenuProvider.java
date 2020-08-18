package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.AppParam;
import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

@Menu
public class ProfileMenuProvider implements MenuProvider {

    Page page = new Page("~./zul/content/userbasic/profile.zul");

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("perfil");
    }

    @Override
    public Page getView() {
        return this.page;
    }

    @Override
    public String getIcon() {
        return "z-icon-user";
    }

    @Override
    public int getPosition() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getGroup() {
        return "datospersonales";
    }
}
