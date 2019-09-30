package com.gitlab.pedrioko.core.zk.viewmodel;

import com.gitlab.pedrioko.core.lang.ProviderAccess;
import com.gitlab.pedrioko.core.lang.annotation.Ckeditor;
import com.gitlab.pedrioko.core.lang.annotation.ColorChooser;
import com.gitlab.pedrioko.core.lang.annotation.Reference;
import com.gitlab.pedrioko.core.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.action.CancelAction;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.api.Valuable;
import com.gitlab.pedrioko.core.view.enums.AplicateAllClass;
import com.gitlab.pedrioko.core.view.enums.CrudAction;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.enums.SubCrudView;
import com.gitlab.pedrioko.core.view.exception.ValidationException;
import com.gitlab.pedrioko.core.view.forms.Form;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.PropertiesUtil;
import com.gitlab.pedrioko.core.view.util.StringUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import com.gitlab.pedrioko.core.zk.component.colorpicker.ColorPicker;
import com.gitlab.pedrioko.services.CrudService;
import com.querydsl.core.types.dsl.PathBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkforge.ckez.CKeditor;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanels;

import javax.persistence.Id;
import javax.persistence.Version;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ProviderAccessFormVM implements Valuable {

    private Object value;
    private CrudActionEvent event;
    private List<String> fields = new ArrayList<>();
    private JSONArray fieldsBase;
    private transient Map<Field, Component> binding = new LinkedHashMap<>();
    private transient Map<String, Component> renglones = new LinkedHashMap<>();
    private transient Map<String, Component> crudviews = new LinkedHashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderAccessFormVM.class);
    private FormStates estado = FormStates.CREATE;
    @Wire
    private Tab tabs;
    @Wire
    private Tabpanels tabpanels;
    @WireVariable
    private List<Action> actions;
    private Class<?> valueClass;
    private transient Map<String, LinkedHashMap<String, Component>> renglonesGroup = new LinkedHashMap<>();

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
        addToList(listmodel, null);
        addToList(listmodel, AplicateAllClass.class);
        addToList(listmodel, SubCrudView.class);
        Object bean = getBean(StringUtil.getDescapitalize(string));
        addToList(listmodel, bean.getClass());
        if (bean instanceof MenuProvider) {
            Component view = ((MenuProvider) bean).getView();
            if (view instanceof CrudView) {
                addToList(listmodel, ((CrudView) view).getTypeClass());
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
        valueClass = value.getClass();

        selectprovider = ((ProviderAccess) value).getMenuprovider();
        if (selectprovider != null) loadPermissions();
        selectpermissions = ((ProviderAccess) value).getActions();
      /*  fieldsBase = ApplicationContextUtils.getBean(PropertiesUtil.class).getFieldForm(valueClass);
        if (fieldsBase != null && !fieldsBase.isEmpty()) {
            fieldsBase.forEach(e -> {
                Object name = ((JSONObject) e).get("name");
                fields.add(name.toString());
                Object customcomponentzul = ((JSONObject) e).get("customcomponentzul");
                Field field = ReflectionJavaUtil.getField(valueClass, ((JSONObject) e).get("name").toString());
                if (customcomponentzul != null) {
                    Component component = Executions.createComponents(customcomponentzul.toString(), null, null);
                    putBinding(field, component);
                    loadReglon(ReflectionZKUtil.getLabel(name.toString()), component);
                } else {
                    this.fieldToUiField(field);
                }
            });
        } else {
            List<Field> listfield = ReflectionJavaUtil.getFields(valueClass).stream()
                    .filter(e -> !e.isAnnotationPresent(Version.class)
                            && !e.getName().equalsIgnoreCase("serialVersionUID")
                            && !e.isAnnotationPresent(Id.class)
                    )
                    .collect(Collectors.toList());
            listfield.forEach(e -> {
                fields.add(e.getName());
                this.fieldToUiField(e);
            });
        }*/

        //setValueForm(value);
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
        try {
            return ReflectionZKUtil.getBindingValue(binding, value.getClass(), value);
        } catch (ValidationException w) {
            LOGGER.error("ERROR on getModel()", w);
            throw w;
        }
    }

    public void setValue(Object value) {
        this.value = value;
    }

/*
    private void fieldToUiField(Field e) {
        Class<?> type = e.getType();
        String label = ReflectionZKUtil.getLabel(e);
        if (e.isAnnotationPresent(Ckeditor.class)) {
            CKeditor c = new CKeditor();
            putBinding(e, c);
            loadReglon(label, c);
            return;
        }
        if (e.isAnnotationPresent(ColorChooser.class)) {
            ColorPicker c = new ColorPicker();
            putBinding(e, c);
            loadReglon(label, c);
            return;
        }
        ApplicationContextUtils.getBeansOfType(FieldComponent.class).stream().filter(v -> v.getToClass() == null
                || v.getToClass().length == 0 || Arrays.asList(v.getToClass()).contains(type)).forEach(w -> {
            Component component = w.getComponent(e);
            if (component != null) {
                putBinding(e, component);
                loadReglon(label, component);
            }

        });
    }*/

    public Component putBinding(String key, Component value) {
        return binding.put(ReflectionJavaUtil.getField(valueClass, key), value);
    }

    public Component putBinding(Field key, Component value) {
        return binding.put(key, value);
    }

    public void loadReglon(String label, Component campo) {
        if (campo instanceof Tabbox)
            crudviews.put(label, campo);
        else {
            renglones.put(label, campo);
            LinkedHashMap<String, Component> list = renglonesGroup.get(campo.getClass().getSimpleName());
            if (list == null)
                list = new LinkedHashMap<>();
            list.put(label, campo);
            renglonesGroup.put(campo.getClass().getSimpleName(), list);
        }
    }

    public Map<Field, Component> getBinding() {
        return binding;
    }

    public void setBinding(Map<Field, Component> binding) {
        this.binding = binding;
    }

    public Map<String, Component> getRenglones() {
        return renglones;
    }

    public void setRenglones(Map<String, Component> renglones) {
        this.renglones = renglones;
    }

    public Map<String, LinkedHashMap<String, Component>> getRenglonesGroup() {
        return renglonesGroup;
    }

    public void setRenglonesGroup(Map<String, LinkedHashMap<String, Component>> renglonesGroup) {
        this.renglonesGroup = renglonesGroup;
    }

    @Override
    @NotifyChange("*")
    public void setValueForm(Object value) {
        CrudService crudService = ApplicationContextUtils.getBean(CrudService.class);
        if (value != null) {
            Object obj = crudService.refresh(value);
            getBinding().forEach((k, v) -> {
                try {
                    Object invoke = ReflectionJavaUtil.getValueFieldObject(k.getName(), obj);
                    if (estado == FormStates.READ)
                        ReflectionZKUtil.disableComponent(v);
                    if (invoke == null)
                        invoke = ReflectionJavaUtil.getNewInstace(k);
                    if (k.isAnnotationPresent(Reference.class)) {
                        Reference annotation = k.getAnnotation(Reference.class);
                        Class<?> annotationvalue = annotation.value();

                        PathBuilder<?> pathBuilder = crudService.getPathBuilder(annotationvalue);
                        String id = annotation.id();
                        PathBuilder<Object> t = null;
                        if (id != null && !id.isEmpty())
                            t = pathBuilder.get(id);
                        else {
                            String idPropertyName = crudService.getIdPropertyName(getValue().getClass());
                            t = pathBuilder.get(idPropertyName);
                        }

                        Object o = invoke != null ? invoke : new ArrayList<>();
                        o = ((List) o).stream().map(l -> Long.parseLong(l.toString())).collect(Collectors.toList());
                        List<?> fetch = crudService.query().from(pathBuilder).where(t.in((List) o)).fetch();
                        ReflectionZKUtil.setValueComponent(v, fetch != null ? fetch : new ArrayList<>());

                    } else
                        ReflectionZKUtil.setValueComponent(v, invoke);
                } catch (SecurityException e) {
                    LOGGER.error("ERROR on setValueForm()", e);
                }
            });

        }
    }

    @Override
    public FormStates getEstado() {
        return estado;
    }

    @Override
    public void setEstado(FormStates estado) {
        this.estado = estado;
    }


    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public Map<String, Component> getCrudviews() {
        return crudviews;
    }

    public void setCrudviews(Map<String, Component> crudviews) {
        this.crudviews = crudviews;
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
