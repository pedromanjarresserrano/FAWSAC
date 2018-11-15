package com.gitlab.pedrioko.core.view.forms;

import com.gitlab.pedrioko.core.lang.annotation.Reference;
import com.gitlab.pedrioko.core.view.action.CancelAction;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.exception.ValidationException;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.services.CrudService;
import com.querydsl.core.types.dsl.PathBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
public @Data
class Form extends Window {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Form.class);
    private FormStates estado = FormStates.CREATE;

    private transient Object newInstance;
    private Div actions = new Div();
    private transient Map<Field, Component> binding = new LinkedHashMap<>();
    private Class<?> klass;
    private String reglonClass = "col-sm-12 col-md-5 col-lg-5";

    public Form() {
        super();
        actions.setClass("col-md-12 col-lg-12 col-xs-12 col-sm-12");
        actions.setStyle("margin-top:10px;margin-buttom:10px;");
    }

    public void addAction(Action action) {
        Button btn = new Button();
        btn.setLabel(action.getLabel());
        btn.setIconSclass(action.getIcon());
        btn.setClass("btn " + action.getClasses());
        btn.addEventListener(Events.ON_CLICK, e -> {

            CrudActionEvent event = new CrudActionEvent();
            event.setValue(getValue());
            event.setFormstate(estado);
            action.actionPerform(event);

        });
        actions.appendChild(btn);

    }

    public Object getValue() {
        try {
            return ReflectionZKUtil.getBindingValue(binding, klass, newInstance);
        } catch (ValidationException w) {
            throw w;
        }
    }

    public void setValueForm(Object obj) {
        CrudService crudService = ApplicationContextUtils.getBean(CrudService.class);
        if (ReflectionJavaUtil.getIdValue(obj) != null && ((long) ReflectionJavaUtil.getIdValue(obj)) != 0)
            setNewInstance(crudService.getById(obj.getClass(), ReflectionJavaUtil.getIdValue(obj)));
        else
            setNewInstance(obj);
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
                        String idPropertyName = crudService.getIdPropertyName(klass);
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

    public void loadReglon(Div renglon, Div labeldiv, Label label, Div campo) {
        labeldiv.appendChild(label);
        renglon.appendChild(labeldiv);
        renglon.setStyle("margin-top:10px;margin-buttom:10px;");
        renglon.setZclass(reglonClass);
        labeldiv.setZclass("col-sm-4");
        campo.setZclass("col-sm-8");
        renglon.appendChild(campo);
    }

    /**
     * @param key
     * @param value
     * @return
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Component putBinding(Field key, Component value) {
        return binding.put(key, value);
    }

    /**
     * @param m
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAllBinding(Map<? extends Field, ? extends Component> m) {
        binding.putAll(m);
    }

    public void addAction(Action action, CrudActionEvent event) {
        addAction(action.getLabel(), action.getIcon(), "btn " + action.getClasses(), e -> {
            try {
                if (!(action instanceof CancelAction)) {
                    Object value = getValue();
                    event.setValue(value);
                }
                action.actionPerform(event);
            } catch (Exception w) {
                LOGGER.error("ERROR on addAction()", w);

            }
        });

    }

    public void addAction(String labelaction, String icon, EventListener<? extends Event> event) {
        addAction(labelaction, icon, "btn btn-primary pull-left", event);
    }

    public void addAction(String labelaction, String icon, String classes, EventListener<? extends Event> event) {
        Button btn = new Button();
        btn.setLabel(labelaction);
        btn.setIconSclass(icon);
        btn.setClass("btn-action " + classes);
        btn.addEventListener(Events.ON_CLICK, event);
        getActions().appendChild(btn);
    }

    /**
     * @return the estado
     */
    public FormStates getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(FormStates estado) {
        this.estado = estado;
        switch (estado) {
            case CREATE:
                try {
                    setValueForm(getKlass().getConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    LOGGER.error("ERROR on setEstado()", e);
                }
                break;
            case READ:
                ReflectionZKUtil.readOnlyBinding(getBinding());
                break;

            case UPDATE:

                break;
            default:
                break;
        }

    }
}
