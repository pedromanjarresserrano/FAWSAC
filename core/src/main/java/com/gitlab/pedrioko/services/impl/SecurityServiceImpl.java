package com.gitlab.pedrioko.services.impl;

import com.gitlab.pedrioko.core.lang.ProviderAccess;
import com.gitlab.pedrioko.core.lang.UserProfile;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.api.Provider;
import com.gitlab.pedrioko.core.view.enums.SubCrudView;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.PropertiesUtil;
import com.gitlab.pedrioko.domain.Usuario;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.services.SecurityService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import static com.gitlab.pedrioko.core.view.util.ApplicationContextUtils.getBeansOfType;
import static java.util.stream.Collectors.groupingBy;

@Service("securityService")
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    PropertiesUtil propertiesUtil;
    @Autowired
    private CrudService crudService;
    private List<MenuProvider> beansOfType;
    private List<Action> actionList;


    private Map<Integer, List<Action>> actionMap;


    private void init() {
        actionMap = getBeansOfType(Action.class).stream().sorted(Comparator.comparing(Action::position)).collect(groupingBy(Action::getGroup));
    }

    @PostConstruct
    private void initMP() {
        List<MenuProvider> beansOfType = getBeansOfType(MenuProvider.class);
        this.beansOfType = beansOfType;
    }

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
        if (menuProvider == null)
            return Collections.emptyList();
        return crudService.refresh(user)
                .getUserprofiles()
                .stream()
                .map(crudService::refresh)
                .map(UserProfile::getProvidersaccess)
                .flatMap(List::stream)
                .filter(e -> e.getMenuprovider().equalsIgnoreCase(menuProvider.getClass().getSimpleName()))
                .flatMap(e -> crudService.refresh(e).getActions().stream())
                .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, List<Action>> getActions(Usuario user, MenuProvider menuProvider, Class klass) {
        return this.getActions(user, menuProvider, klass, Collections.EMPTY_LIST);
    }

    @Override
    public Map<Integer, List<Action>> getActions(Usuario user, MenuProvider menuProvider, Class<?> klass, List<Class<?>> classList) {
        boolean enableCommonActionsClass = propertiesUtil
                .getEnableCommonActionsClass(klass);
        boolean enableSubCrudsClass = classList.contains(SubCrudView.class) ? propertiesUtil.getEnableSubCrudsClass(klass, true) : true;
        List<String> permission = new ArrayList<>();
        Map<Integer, List<Action>> actionsList = new LinkedHashMap<>();
        if (user.getTipo() != TipoUsuario.ROLE_ADMIN) {
            permission.addAll(getPermission(user, menuProvider));
        }
        if (actionMap == null || actionMap.isEmpty())
            init();

        actionMap.forEach((k, v) -> {
            List<Action> actions = new ArrayList<>();
            for (Action e : v) {
                if (permission.contains(e.getClass().getSimpleName()) || user.getTipo() == TipoUsuario.ROLE_ADMIN) {
                    if (CollectionUtils.containsAny(e.getAplicateClass(), classList)) {
                        if (k != 0 || !e.isDefault() || enableCommonActionsClass) {
                            if (enableSubCrudsClass && k == 0)
                                actions.add(e);
                            else if (k > 0) {
                                actions.add(e);
                            }
                        }
                    }
                }
            }
            actionsList.put(k, actions);

        });

        return actionsList;
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
        return getProvider(user).stream()
                .collect(Collectors.groupingBy((MenuProvider menuProvider) -> menuProvider.getGroup() != null ? ApplicationContextUtils.getBean(menuProvider.getGroup()).getLabel() : ""));
    }

    @Override
    public Map<Class<? extends Provider>, List<MenuProvider>> getProviderGroupByGroup(Usuario user) {
        return getProvider(user)
                .stream()
                .collect(Collectors.groupingBy(MenuProvider::getGroup));
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
