package com.example.providers;

import com.example.domain.Item;
import org.springframework.stereotype.Component;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import org.zkoss.zk.ui.Component;

@Component
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
        return 0;
    }

    @Override
    public String getGroup() {
        return "Inventario";
    }
}
