package com.gitlab.pedrioko.providers.menu;

import com.gitlab.pedrioko.core.lang.Page;
import org.springframework.stereotype.Component;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.api.Provider;
import com.gitlab.pedrioko.domain.Usuario;
import com.gitlab.pedrioko.providers.AdminGroupProvider;

@Component
public class UsuariosMenuProvider implements MenuProvider {

    Page page = new Page(Usuario.class);

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("usuarios");
    }

    @Override
    public Page getView() {
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
    public Class<? extends Provider> getGroup() {
        return AdminGroupProvider.class;
    }
}
