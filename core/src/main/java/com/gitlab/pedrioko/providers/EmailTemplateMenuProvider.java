package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import com.gitlab.pedrioko.domain.EmailTemplate;
import com.gitlab.pedrioko.domain.Reporte;
import org.zkoss.zk.ui.Component;

@Menu
public class EmailTemplateMenuProvider implements MenuProvider {

    Page page = new Page(EmailTemplate.class);


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
        return Integer.MAX_VALUE;
    }

    @Override
    public String getGroup() {
        return "administracion";
    }
}
