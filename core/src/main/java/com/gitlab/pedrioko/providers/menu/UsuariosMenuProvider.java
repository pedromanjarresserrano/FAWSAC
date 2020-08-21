package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.domain.Usuario;

@Menu
public class UsuariosMenuProvider implements MenuProvider {

    Page page = new Page(Usuario.class);

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
    public Class<?> getGroup() {
        return "administracion";
    }
}