package com.gitlab.pedrioko.providers.menu;

import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.api.Provider;
import com.gitlab.pedrioko.domain.EmailTemplate;
import com.gitlab.pedrioko.providers.AdminGroupProvider;

@Menu
public class EmailTemplateMenuProvider implements MenuProvider {

    Page page = new Page(EmailTemplate.class);


    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("EmailTemplate");
    }

    @Override
    public Page getView() {
        return this.page;
    }

    @Override
    public String getIcon() {
        return "fas fa-envelope-square";
    }

    @Override
    public int getPosition() {
        return Integer.MAX_VALUE - 4;
    }

    @Override
    public Class<? extends Provider> getGroup() {
        return AdminGroupProvider.class;
    }
}
