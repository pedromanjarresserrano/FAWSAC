package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.UserProfile;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import org.zkoss.zk.ui.Component;

@Menu
public class UserProfileMenuProvider implements MenuProvider {

    Page page = new Page(UserProfile.class);

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
    public String getGroup() {
        return "administracion";
    }
}
