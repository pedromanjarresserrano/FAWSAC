package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import com.gitlab.pedrioko.domain.LoginLog;
import org.zkoss.zk.ui.Component;

import java.util.HashMap;
import java.util.Map;

@Menu
public class LogUsuarioMenuProvider implements MenuProvider {

    Page page = new Page(LoginLog.class);

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("Usuarios Login Log");
    }

    @Override
    public Page getView() {

        HashMap<Object, Object> paramsroot = new HashMap<>();
        paramsroot.put("user", ApplicationContextUtils.getBean(FHSessionUtil.class).getCurrentUser());
        this.page.setArg(paramsroot);
        return this.page;
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
