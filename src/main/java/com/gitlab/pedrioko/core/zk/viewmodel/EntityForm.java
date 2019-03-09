package com.gitlab.pedrioko.core.zk.viewmodel;

import com.gitlab.pedrioko.core.lang.annotation.Ckeditor;
import com.gitlab.pedrioko.core.lang.annotation.ColorChooser;
import com.gitlab.pedrioko.core.lang.annotation.Reference;
import com.gitlab.pedrioko.core.view.action.CancelAction;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import com.gitlab.pedrioko.core.view.api.Valuable;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.exception.ValidationException;
import com.gitlab.pedrioko.core.view.forms.Form;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.PropertiesUtil;
import com.gitlab.pedrioko.core.zk.component.ColorChooserBox;
import com.gitlab.pedrioko.services.CrudService;
import com.querydsl.core.types.dsl.PathBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkforge.ckez.CKeditor;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanels;

import javax.persistence.Id;
import javax.persistence.Version;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;


@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EntityForm implements Valuable {

    private Object value;
    CrudActionEvent event;
    private List<String> fields;
    private transient Map<Field, Component> binding = new LinkedHashMap<>();
    private transient Map<String, Component> renglones = new LinkedHashMap<>();
    private transient Map<String, Component> crudviews = new LinkedHashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityForm.class);
    private FormStates estado = FormStates.CREATE;
    @Wire
    private Tab tabs;
    @Wire
    private Tabpanels tabpanels;
    @WireVariable
    private List<Action> actions;
    private AnnotateDataBinder binder;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component comp) throws Exception {
        binder = new AnnotateDataBinder(comp);
        init();
    }

    public void init() {
        value = Executions.getCurrent().getArg().get("value");
        binder.bindBean("value", value);
        event = (CrudActionEvent) Executions.getCurrent().getArg().get("event-crud");
        estado = (FormStates) Executions.getCurrent().getArg().get("estado-form");

        Class<?> aClass = value.getClass();
        fields = ApplicationContextUtils.getBean(PropertiesUtil.class).getFieldForm(aClass);
        List<Field> listfield = new ArrayList<>();
        if (fields != null && !fields.isEmpty()) {
            List<Field> finalListfield = listfield;
            fields.forEach(e -> {
                finalListfield.add(ReflectionJavaUtil.getField(aClass, e));
            });
        } else {
            listfield = ReflectionJavaUtil.getFields(aClass).stream()
                    .filter(e -> !e.isAnnotationPresent(Version.class) && !e.getName().equalsIgnoreCase("serialVersionUID")
                            && !e.isAnnotationPresent(Id.class)
                            && (fields != null && !fields.isEmpty() ? fields.contains(e.getName()) : true))
                    .collect(Collectors.toList());
        }
        listfield.forEach(this::fieldToUiField);
        //   setValueForm(value);
        List<Action> beans = ApplicationContextUtils.getBeans(Action.class);
        actions = beans.stream().filter(e -> e.getAplicateClass().contains(Form.class)).collect(Collectors.toList());
        actions.sort(Comparator.comparing(Action::position));
        binder.loadAll();
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
                Object value = getValue();
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
            return value/* ReflectionZKUtil.getBindingValue(binding, value.getClass(), value)*/;
        } catch (ValidationException w) {
            LOGGER.error("ERROR on getValue()", w);
            throw w;
        }
    }

    public void setValue(Object value) {
        this.value = value;
    }


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
            ColorChooserBox c = new ColorChooserBox();
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
                binder.addBinding(component, "value", "value." + e.getName(), new String[]{}, "self.onChange", "both", null);
            }

        });
/*
        if (e.isAnnotationPresent(NoEmpty.class)) {
            Label requerido = new Label(" (" + ReflectionZKUtil.getLabel("requerido") + ")");
            requerido.setClass("text-danger");
        }*/
      /*ApplicationContextUtils.getBeansOfType(RowCustomizer.class).stream().filter(f -> f.getToClass().contains(type))
                .forEach(c -> c.customizer(renglon, e));*/

    }

    public Component putBinding(Field key, Component value) {
        return binding.put(key, value);
    }

    public void loadReglon(String label, Component campo) {
        if (campo instanceof Tabbox)
            crudviews.put(label, campo);
        else
            renglones.put(label, campo);
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

    @Override
    public void setValueForm(Object obj) {
        CrudService crudService = ApplicationContextUtils.getBean(CrudService.class);
        getBinding().forEach((k, v) -> {
            try {
                Object invoke = ReflectionJavaUtil.getValueFieldObject(k.getName(), obj);
                if (estado == FormStates.READ)
                    ReflectionZKUtil.disableComponent(v);
                if (invoke == null)
                    invoke = ReflectionJavaUtil.getNewInstace(k);
                if (k.isAnnotationPresent(Reference.class)) {
                    Reference annotation = k.getAnnotation(Reference.class);
                    Class<?> value = annotation.value();

                    PathBuilder<?> pathBuilder = crudService.getPathBuilder(value);
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
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("ERROR on setValueForm()", e);
            }
        });

    }

    @Override
    public FormStates getEstado() {
        return estado;
    }

    @Override
    public void setEstado(FormStates estado) {
        this.estado = estado;
    }

    public CrudActionEvent getEvent() {
        return event;
    }

    public void setEvent(CrudActionEvent event) {
        this.event = event;
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
}
