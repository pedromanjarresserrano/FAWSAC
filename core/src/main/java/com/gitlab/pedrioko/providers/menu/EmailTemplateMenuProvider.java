package com.gitlab.pedrioko.providers.menu;

import com.gitlab.pedrioko.core.lang.Page;
import org.springframework.stereotype.Component;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.api.Provider;
import com.gitlab.pedrioko.domain.EmailTemplate;
import com.gitlab.pedrioko.providers.AdminGroupProvider;

@Component
public class EmailTemplateMenuProvider implements MenuProvider {

    Page page = new Page(EmailTemplate.class);


    @Override
    public String getName() {
        return null;
    }

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
