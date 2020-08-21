package com.gitlab.pedrioko.services;

import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.api.Provider;
import com.gitlab.pedrioko.domain.Usuario;

import java.util.List;
import java.util.Map;

public interface SecurityService {
    List<String> getAccess(Usuario user);

    List<String> getPermission(Usuario user, MenuProvider menuProvider);

    Map<Integer, List<Action>> getActions(Usuario user, MenuProvider menuProvider, Class klass);

    Map<Integer, List<Action>> getActions(Usuario user, MenuProvider menuProvider, Class<?> klass, List<Class<?>> classList);

    List<MenuProvider> getProvider(Usuario user);

    Map<String, List<MenuProvider>> getProviderGroup(Usuario user);

    Map<Class<? extends Provider>, List<MenuProvider>> getProviderGroupByGroup(Usuario user);

    boolean haveAccess(Usuario user, Class<MenuProvider> menuProvider);

    boolean haveAccess(Usuario user, MenuProvider menuProvider);
}
