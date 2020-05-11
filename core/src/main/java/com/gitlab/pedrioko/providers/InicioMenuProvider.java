package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import org.springframework.core.annotation.Order;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

@Menu
@Order(0)
public class InicioMenuProvider implements MenuProvider {

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("Dashboard");
    }

    @Override
    public Component getView() {
        return Executions.createComponents("~./zul/content/dashboard/page.zul", null, null);
    }

    @Override
    public String getIcon() {
        return "z-icon-home";
    }

    @Override
    public int getPosition() {
        return -1;
    }

    @Override
    public String getGroup() {
        return "";
    }

    @Override
    public boolean isOpenByDefault() {
        return true;
    }
}
