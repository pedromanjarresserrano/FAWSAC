package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

//@Menu
public class ProfileMenuProvider implements MenuProvider {

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("perfil");
    }

    @Override
    public Component getView() {
        return Executions.createComponents("~./zul/content/userbasic/profile.zul", null, null);
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
