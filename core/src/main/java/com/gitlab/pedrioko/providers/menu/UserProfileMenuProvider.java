package com.gitlab.pedrioko.providers.menu;

import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.UserProfile;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.api.Provider;
import com.gitlab.pedrioko.providers.AdminGroupProvider;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMenuProvider implements MenuProvider {

    Page page = new Page(UserProfile.class);

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("UserProfile");
    }

    @Override
    public Page getView() {
        return this.page;
    }

    @Override
    public String getIcon() {
        return "fa fa-lock";
    }

    @Override
    public int getPosition() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Class<? extends Provider> getGroup() {
        return AdminGroupProvider.class;
    }
}
