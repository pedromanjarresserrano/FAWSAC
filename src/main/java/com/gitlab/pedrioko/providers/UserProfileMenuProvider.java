package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.UserProfile;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.viewers.CrudView;
import org.zkoss.zk.ui.Component;

@Menu
public class UserProfileMenuProvider implements MenuProvider {

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("UserProfile");
    }

    @Override
    public Component getView() {
        return new CrudView(UserProfile.class);
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
