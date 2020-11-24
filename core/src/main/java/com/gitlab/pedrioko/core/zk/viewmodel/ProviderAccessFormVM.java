package com.gitlab.pedrioko.core.zk.viewmodel;

import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.ProviderAccess;
import com.gitlab.pedrioko.core.view.action.CancelAction;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.api.Valuable;
import com.gitlab.pedrioko.core.view.enums.AplicateAllClass;
import com.gitlab.pedrioko.core.view.enums.CrudAction;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.enums.SubCrudView;
import com.gitlab.pedrioko.core.view.forms.Form;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanels;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ProviderAccessFormVM implements Valuable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderAccessFormVM.class);
    private transient Object value;
    private CrudActionEvent event;
    private FormStates estado = FormStates.CREATE;
    @Wire
    private Tab tabs;
    @Wire
    private Tabpanels tabpanels;
    @WireVariable
    private List<Action> actions;

    private List<String> listproviders = new ArrayList<>();
    private String selectprovider = new String();
    private List<String> permissions = new ArrayList<>();
    private List<Action> beans;
    private List<String> selectpermissions = new ArrayList<>();

    @Command("loadpermissions")
    @NotifyChange("permissions")
    public void loadPermissions() {
        permissions.clear();
        loadActions(permissions, selectprovider);
    }

    private void loadActions(List<String> listmodel, String string) {

        Object bean = getBean(StringUtil.getDescapitalize(string));
        if (bean instanceof MenuProvider) {
            Page page = ((MenuProvider) bean).getView();
            if (page.getUriView() == null || page.getUriView().isEmpty()) {
                addToList(listmodel, null);
                addToList(listmodel, SubCrudView.class);
                addToList(listmodel, bean.getClass());

                Class view = page.getPageClass();
                if (view != null) {
                    addToList(listmodel, view);
                }
            }

        }
    }

    private Object getBean(String arg0) {
        return ApplicationContextUtils.getBean(arg0);
    }

    private void addToList(List listmodel, Class<?> klass) {
        beans.stream().filter(x -> x.getAplicateClass() != null)
                .filter(x -> x.getAplicateClass().contains(klass)).map(Action::getClass)
                .map(Class::getSimpleName).forEach(listmodel::add);
        if (klass == null) {
            beans.stream()
                    .filter(x -> x.getAplicateClass().contains(CrudAction.class) || x.getAplicateClass().contains(AplicateAllClass.class))
                    .map(Action::getClass).map(Class::getSimpleName)
                    .forEach(listmodel::add);
        }
    }

    @Init
    public void init() {
        value = Executions.getCurrent().getArg().get("value");
        event = (CrudActionEvent) Executions.getCurrent().getArg().get("event-crud");
        estado = (FormStates) Executions.getCurrent().getArg().get("estado-form");
        beans = ApplicationContextUtils.getBeans(Action.class);
        listproviders = ApplicationContextUtils.getBeans(MenuProvider.class).stream()
                .map(MenuProvider::getClass)
                .map(Class::getSimpleName)
                .collect(Collectors.toList());

        selectprovider = ((ProviderAccess) value).getMenuprovider();
        if (selectprovider != null) loadPermissions();
        selectpermissions = ((ProviderAccess) value).getActions();

        List<Action> actions = (List<Action>) Executions.getCurrent().getArg().get("actions-form");
        if (actions == null) {
            List<Action> beans = ApplicationContextUtils.getBeans(Action.class);
            this.actions = beans.stream().filter(e -> e.getAplicateClass().contains(Form.class)).collect(Collectors.toList());
        } else
            this.actions = actions;
        this.actions.sort(Comparator.comparing(Action::position));
        if (estado == FormStates.READ) {
            this.actions.clear();
        }
    }

    public List<String> getListproviders() {
        return listproviders;
    }

    public void setListproviders(List<String> listproviders) {
        this.listproviders = listproviders;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @Command
    public void clickAction(@BindingParam("action") Action action) {
        try {
            if (!(action instanceof CancelAction)) {
                ((ProviderAccess) value).setMenuprovider(selectprovider);
                ((ProviderAccess) value).setActions(selectpermissions);
                event.setSource(this);
                event.setValue(value);
            }
            action.actionPerform(event);
        } catch (Exception w) {
            LOGGER.error("ERROR on addAction()", w);

        }
    }

    public Object getValue() {
        return value;

    }

    public void setValue(Object value) {
        this.value = value;
        selectprovider = ((ProviderAccess) value).getMenuprovider();
        if (selectprovider != null) loadPermissions();
        selectpermissions = ((ProviderAccess) value).getActions();
    }


    @Override
    @NotifyChange("*")
    public void setValueForm(Object value) {
        setValue(value);
    }

    @Override
    public FormStates getEstado() {
        return estado;
    }

    @Override
    public void setEstado(FormStates estado) {
        this.estado = estado;
    }

    public Tab getTabs() {
        return tabs;
    }

    public void setTabs(Tab tabs) {
        this.tabs = tabs;
    }

    public Tabpanels getTabpanels() {
        return tabpanels;
    }

    public void setTabpanels(Tabpanels tabpanels) {
        this.tabpanels = tabpanels;
    }

    public String getSelectprovider() {
        return selectprovider;
    }

    public void setSelectprovider(String selectprovider) {
        this.selectprovider = selectprovider;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<String> getSelectpermissions() {
        return selectpermissions;
    }

    public void setSelectpermissions(List<String> selectpermissions) {
        this.selectpermissions = selectpermissions;
    }
}
