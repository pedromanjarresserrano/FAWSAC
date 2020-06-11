package com.gitlab.providers;

import com.gitlab.domain.Item;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import org.zkoss.zk.ui.Component;

@Menu
public class ItemMenuProvider implements MenuProvider {
    @Override
    public String getLabel() {
        return "Items";
    }

    @Override
    public Component getView() {
        return new CrudView(Item.class);
    }

    @Override
    public String getIcon() {
        return "fas fa-boxes";
    }

    @Override
    public int getPosition() {
        return -1;
    }

    @Override
    public String getGroup() {
        return "Inventario";
    }
}
