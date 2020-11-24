package com.gitlab.pedrioko.providers.menu;

import com.gitlab.pedrioko.core.lang.Page;
import org.springframework.stereotype.Component;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.api.Provider;
import com.gitlab.pedrioko.domain.Reporte;
import com.gitlab.pedrioko.providers.StatsGroupProvider;

@Component
public class ReportesMenuProvider implements MenuProvider {

    Page page = new Page(Reporte.class);

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("reportes");
    }

    @Override
    public Page getView() {
        return this.page;
    }

    @Override
    public String getIcon() {
        return "fas fa-chart-pie";
    }

    @Override
    public int getPosition() {
        return 1;
    }

    @Override
    public Class<? extends Provider> getGroup() {
        return StatsGroupProvider.class;
    }

}