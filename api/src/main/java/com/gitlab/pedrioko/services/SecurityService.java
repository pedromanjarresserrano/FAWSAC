package com.gitlab.pedrioko.services;

import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.domain.Usuario;

import java.util.List;
import java.util.Map;

public interface SecurityService {
    List<String> getAccess(Usuario user);

    List<String> getPermission(Usuario user, MenuProvider menuProvider);

    List<MenuProvider> getProvider(Usuario user);

    Map<String, List<MenuProvider>> getProviderGroup(Usuario user);

    boolean haveAccess(Usuario user, Class<MenuProvider> menuProvider);

    boolean haveAccess(Usuario user, MenuProvider menuProvider);
}
