package com.gitlab.pedrioko.providers.menu;

import com.gitlab.pedrioko.core.lang.AuditLog;
import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.api.Provider;
import com.gitlab.pedrioko.providers.AdminGroupProvider;
import org.springframework.stereotype.Component;

@Component
public class AuditMenuProvider implements MenuProvider {
    Page page = new Page(AuditLog.class);

    @Override
    public String getName() {
        return "AuditMenuProvider";
    }

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
        return 1;
    }

    @Override
    public Class<? extends Provider> getGroup() {
        return AdminGroupProvider.class;
    }
}
