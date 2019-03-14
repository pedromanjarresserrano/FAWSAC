package com.gitlab.pedrioko.core.zk.viewmodel.crud;

import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.api.CrudDisplayTable;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.enums.*;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
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
import org.zkoss.zk.ui.Executions;
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
    private Map<Integer, List<Action>> actions = new LinkedHashMap<>();
    private CrudDisplayTable crudDisplayTable;
    private boolean enableCommonActionsClass;
    private CrudView crudView;
    private CrudService crudService;
    @WireVariable
    private transient FHSessionUtil fhSessionUtil;
    private MenuProvider menuprovider;
    private List<String> strings = new ArrayList<>();

    @Init
    private void init() {
        crudView = (CrudView) Executions.getCurrent().getArg().get("CrudView");
        klass = (Class<?>) Executions.getCurrent().getArg().get("klass-crud");
        crudDisplayTable = (CrudDisplayTable) Executions.getCurrent().getArg().get("crudTable");
        menuprovider = (MenuProvider) Executions.getCurrent().getArg().get("menuprovider");
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
                        if (k == 0 && e.isDefault() && !enableCommonActionsClass) {
                            continue;
                        } else {
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
    }

    public Class<?> getKlass() {
        return klass;
    }

    public String getId() {
        return "crudviewbar-" + klass.getSimpleName();
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

    public CrudDisplayTable getCrudDisplayTable() {
        return crudDisplayTable;
    }

    public void setCrudDisplayTable(CrudDisplayTable crudDisplayTable) {
        this.crudDisplayTable = crudDisplayTable;
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

    @Command
    public void clickAction(@BindingParam("action") Action action) {
        CrudActionEvent event = new CrudActionEvent();
        event.setCrudViewParent(crudView);
        Object selectedValue = crudService.refresh(crudDisplayTable.getSelectedValue());
        if (action.getFormState() == FormStates.CREATE)
            event.setValue(ReflectionJavaUtil.getNewInstace(klass));
        else {
            event.setValue(selectedValue);
        }
        event.setFormstate(action.getFormState());
        action.actionPerform(event);
    }

}
