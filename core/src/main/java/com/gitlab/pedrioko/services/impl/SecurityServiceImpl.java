package com.gitlab.pedrioko.services.impl;

import com.gitlab.pedrioko.core.lang.ProviderAccess;
import com.gitlab.pedrioko.core.lang.UserProfile;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.domain.Usuario;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("securityService")
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private CrudService crudService;

    @Autowired
    private List<MenuProvider> beansOfType;

    @Override
    public List<String> getAccess(Usuario user) {
        return crudService.refresh(user)
                .getUserprofiles()
                .stream()
                .map(crudService::refresh)
                .map(UserProfile::getProvidersaccess)
                .flatMap(List::stream)
                .map(ProviderAccess::getMenuprovider)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getPermission(Usuario user, MenuProvider menuProvider) {
        return crudService.refresh(user)
                .getUserprofiles()
                .stream()
                .map(crudService::refresh)
                .map(UserProfile::getProvidersaccess)
                .flatMap(List::stream)
                .filter(e -> {
                    return e.getMenuprovider().equalsIgnoreCase(menuProvider.getClass().getSimpleName());
                })
                .flatMap(e -> crudService.refresh(e).getActions().stream())
                .collect(Collectors.toList());
    }


    @Override
    public List<MenuProvider> getProvider(Usuario user) {
        if (user.getTipo() == TipoUsuario.ROLE_ADMIN) {
            return beansOfType;
        } else {
            List<String> access = getAccess(user);
            return beansOfType.stream()
                    .filter(e -> access.contains(e.getClass().getSimpleName()))
                    .sorted(Comparator.comparingInt(MenuProvider::getPosition))
                    .collect(Collectors.toList());
        }

    }

    @Override
    public Map<String, List<MenuProvider>> getProviderGroup(Usuario user) {
        return getProvider(user).stream().collect(Collectors.groupingBy(MenuProvider::getGroup));
    }

    @Override
    public boolean haveAccess(Usuario user, Class<MenuProvider> menuProvider) {
        return user.getTipo() == TipoUsuario.ROLE_ADMIN || getAccess(user).contains(menuProvider.getSimpleName());
    }

    @Override
    public boolean haveAccess(Usuario user, MenuProvider menuProvider) {
        return haveAccess(user, (Class<MenuProvider>) menuProvider.getClass());
    }
}
