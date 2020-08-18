package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.domain.EmailTemplate;
import org.springframework.core.annotation.Order;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

@Menu
@Order(0)
public class InicioMenuProvider implements MenuProvider {
    Page page = new Page("~./zul/content/dashboard/page.zul");

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("Dashboard");
    }

    @Override
    public Page getView() {
        return this.page;
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
