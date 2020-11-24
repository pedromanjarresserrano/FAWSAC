package com.gitlab.pedrioko.providers.menu;

import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.Report;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.api.Provider;
import com.gitlab.pedrioko.providers.StatsGroupProvider;
import org.springframework.stereotype.Component;

@Component
public class ReportsMenuProvider implements MenuProvider {
    Page page = new Page(Report.class);

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("Print Reports");
    }

    @Override
    public Page getView() {
        return page;
    }

    @Override
    public String getIcon() {
        return "fas fa-print";
    }

    @Override
    public int getPosition() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Class<? extends Provider> getGroup() {
        return StatsGroupProvider.class;
    }
}
