package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import com.gitlab.pedrioko.domain.LoginLog;
import org.zkoss.zk.ui.Component;

@Menu
public class LogUsuarioMenuProvider implements MenuProvider {

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("Usuarios Login Log");
    }

    @Override
    public Component getView() {
        CrudView crudView = new CrudView(LoginLog.class);
        crudView.addRootParams("user", ApplicationContextUtils.getBean(FHSessionUtil.class).getCurrentUser());
        return crudView;
    }

    @Override
    public String getIcon() {
        return "fa fa-users";
    }

    @Override
    public int getPosition() {
        return 2;
    }

    @Override
    public String getGroup() {
        return "administracion";
    }
}
