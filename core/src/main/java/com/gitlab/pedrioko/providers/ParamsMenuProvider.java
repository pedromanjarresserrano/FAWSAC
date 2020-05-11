package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.AppParam;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import org.zkoss.zk.ui.Component;

@Menu
public class ParamsMenuProvider implements MenuProvider {

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("Params");
    }

    @Override
    public Component getView() {
        return new CrudView(AppParam.class);
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
