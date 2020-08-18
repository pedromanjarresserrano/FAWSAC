package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.AuditLog;
import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import org.zkoss.zk.ui.Component;

@Menu
public class AuditMenuProvider implements MenuProvider {
    Page page = new Page(AuditLog.class);
    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("Log");
    }

    @Override
    public Page getView() {
        return page;
    }

    @Override
    public String getIcon() {
        return "fa fa-users";
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
