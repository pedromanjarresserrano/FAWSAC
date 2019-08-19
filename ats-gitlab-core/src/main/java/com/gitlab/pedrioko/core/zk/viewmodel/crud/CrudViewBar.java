package com.gitlab.pedrioko.core.zk.viewmodel.crud;

import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.api.ToolbarFilter;
import com.gitlab.pedrioko.core.view.enums.AplicateAllClass;
import com.gitlab.pedrioko.core.view.enums.CrudAction;
import com.gitlab.pedrioko.core.view.enums.CrudMode;
import com.gitlab.pedrioko.core.view.enums.SubCrudView;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.core.view.util.PropertiesUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import com.gitlab.pedrioko.services.CrudService;
import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.*;
import java.util.stream.Collectors;

import static com.gitlab.pedrioko.core.view.util.ApplicationContextUtils.getBean;
import static com.gitlab.pedrioko.core.view.util.ApplicationContextUtils.getBeansOfType;
import static java.util.stream.Collectors.groupingBy;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class CrudViewBar {
    private Class<?> klass;
    private List<Action> crudsActions = new ArrayList<>();
    private Map<String, Component> filters = new LinkedHashMap<>();
    private Map<Integer, List<Action>> actions = new LinkedHashMap<>();
    private boolean enableCommonActionsClass;
    private CrudView crudView;
    private CrudService crudService;
    @WireVariable
    private transient FHSessionUtil fhSessionUtil;
    private MenuProvider menuprovider;
    private List<String> strings = new ArrayList<>();
    private String uuid;
    private final transient Map<String, Component> binding = new LinkedHashMap<String, Component>();

    @Init
    private void init() {
        crudView = (CrudView) Executions.getCurrent().getArg().get("CrudView");
        klass = (Class<?>) Executions.getCurrent().getArg().get("klass-crud");
        menuprovider = (MenuProvider) Executions.getCurrent().getArg().get("menuprovider");
        uuid = (String) Executions.getCurrent().getArg().get("CrudViewUUID");
        enableCommonActionsClass = ApplicationContextUtils.getBean(PropertiesUtil.class)
                .getEnableCommonActionsClass(klass);
        crudService = getBean(CrudService.class);
        fhSessionUtil = getBean(FHSessionUtil.class);
        Map<Integer, List<Action>> listMap = getBeansOfType(Action.class).stream().sorted(Comparator.comparing(Action::position)).collect(groupingBy(Action::getGroup));
        if (fhSessionUtil.getCurrentUser().getTipo() != TipoUsuario.ROLE_ADMIN) {
            strings.addAll(fhSessionUtil.getCurrentUser().getUserprofiles().stream().flatMap(e -> e.getProvidersaccess().stream())
                    .filter(e -> e.getMenuprovider().equalsIgnoreCase(menuprovider.getClass().getSimpleName()))
                    .flatMap(e -> e.getActions().stream()).collect(Collectors.toList()));
        }

        listMap.forEach((k, v) -> {
            List<Action> actions = new ArrayList<>();
            for (Action e : v) {
                if (strings.contains(e.getClass().getSimpleName()) || fhSessionUtil.getCurrentUser().getTipo() == TipoUsuario.ROLE_ADMIN) {
                    Class<?> subCrudViewClass = crudView.getCrudviewmode() == CrudMode.SUBCRUD ? SubCrudView.class : ApplicationContextUtils.class;
                    if (CollectionUtils.containsAny(e.getAplicateClass(), Arrays.asList(CrudAction.class, klass, AplicateAllClass.class, subCrudViewClass))) {
                        if (k != 0 || !e.isDefault() || enableCommonActionsClass) {
                            if (e.isDefault() && k == 0) {
                                crudsActions.add(e);
                            }
                            actions.add(e);
                        }
                    }
                }
            }
            this.actions.put(k, actions);
        });
        ApplicationContextUtils.getBeansOfType(ToolbarFilter.class)
                .stream()
                .filter(v -> v.getAplicateClass() == null ||
                        v.getAplicateClass().size() == 0 ||
                        v.getAplicateClass().contains(klass) ||
                        v.getAplicateClass().stream().anyMatch(t -> t.isAssignableFrom(klass))).forEach(w -> {
            Component component = w.getComponent();
            if (component != null) {
                putBinding(w.getField(), component);
                filters.put(ReflectionZKUtil.getLabel(w.getField()), component);
                component.addEventListener(Events.ON_CHANGE, e -> {
                    crudView.getCrudController().put(w.getField(), ReflectionZKUtil.getValue(component));
                //    crudView.getCrudController().doQuery();
                });
                component.addEventListener(Events.ON_OK, e -> {
                    crudView.getCrudController().put(w.getField(), ReflectionZKUtil.getValue(component));
                    crudView.getCrudController().doQuery();
                });
            }
        });
    }

    private Component putBinding(String key, Component value) {
        return binding.put(key, value);
    }

    public Class<?> getKlass() {
        return klass;
    }

    public String getId() {
        return "crudviewbar-" + klass.getSimpleName() + uuid;
    }

    public void setKlass(Class<?> klass) {
        this.klass = klass;
    }

    public List<Action> getCrudsActions() {
        return crudsActions;
    }

    public Map<Integer, List<Action>> getActions() {
        return actions;
    }

    public boolean isEnableCommonActionsClass() {
        return enableCommonActionsClass;
    }

    public void setEnableCommonActionsClass(boolean enableCommonActionsClass) {
        this.enableCommonActionsClass = enableCommonActionsClass;
    }

    public CrudView getCrudView() {
        return crudView;
    }

    public void setCrudView(CrudView crudView) {
        this.crudView = crudView;
    }

    public String generateId(Action action) {
        return klass.getSimpleName() + "-" + action.getClass().getSimpleName() + "-" + UUID.randomUUID().toString();
    }

    public Map<String, Component> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Component> filters) {
        this.filters = filters;
    }

    @Command
    public void searchAction(@BindingParam("valuesearch") String searchvalue) {
        crudView.getCrudController().setContainsString(searchvalue);
    }

    @Command
    public void clickAction(@BindingParam("action") Action action) {
        EventQueues.lookup("action-crud-" + klass.getSimpleName(), EventQueues.SESSION, true).publish(new Event("action-crud-" + klass.getSimpleName() + "-" + uuid, crudView, action));
    }

}
