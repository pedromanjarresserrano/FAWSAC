package com.gitlab.pedrioko.core.view.viewers;

import com.gitlab.pedrioko.core.view.api.FieldFilterComponent;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import lombok.Getter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;

import javax.persistence.Id;
import javax.persistence.Version;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class CrudFilters extends Window {
    private static final String CLASSES = "col-xs-12 col-sm-12 col-md-12 col-lg-12";
    private final String reglonClass = "control-group col-sm-12 col-md-12 col-lg-12";
    private final transient Map<Field, Component> binding = new LinkedHashMap<>();
    private @Getter
    final Class<?> klass;
    private List<String> fieldsfilters;

    public CrudFilters(Class<?> klass, CrudView parent) {
        this.klass = klass;
        List<Field> listfield = ReflectionJavaUtil.getFields(getKlass()).stream()
                .filter(e -> !e.isAnnotationPresent(Version.class) && !e.getName().equalsIgnoreCase("serialVersionUID")
                        && !e.isAnnotationPresent(Id.class)
                        && (fieldsfilters != null && !fieldsfilters.isEmpty() ? fieldsfilters.contains(e.getName()) : true))
                .collect(Collectors.toList());
        Vlayout filters = new Vlayout();

        listfield.forEach(e -> filters.appendChild(fieldToUiField(e)));
        appendChild(filters);
        Div buttons = new Div();
        Button child = new Button(ReflectionZKUtil.getLabel("buscarbtn"));
        child.setClass("btn btn-primary pull-left");
        child.setId(new Random().toString());
        child.setStyle("margin-top:15px; margin-left:20px; margin-bottom:20px;");
        child.addEventListener(Events.ON_CLICK, (event) -> {
            parent.clearParams();
            binding.forEach((k, v) -> {
                Object valueComponent = ReflectionZKUtil.getValueComponent(v);
                if (valueComponent != null)
                    if (valueComponent instanceof String) {
                        if (!((String) valueComponent).isEmpty())
                            parent.addParams(k.getName(), valueComponent);
                    } else parent.addParams(k.getName(), valueComponent);

            });
            parent.getCrudController().doQuery();
        });
        buttons.appendChild(child);
        appendChild(filters);
        filters.setStyle("overflow-y: auto !important;");
        filters.setHeight("80%");
        buttons.setStyle("padding:8px;");
        appendChild(buttons);
        // setHeight("100%");
        buttons.setWidth("100%");
    }

    private Component fieldToUiField(Field e) {
        Div renglon = new Div();
        renglon.setId("Reglon" + e.getType().toGenericString());
        Div labeldiv = new Div();
        Label label = new Label(ReflectionZKUtil.getLabel(e));

        Div campo = new Div();
        Class<?> type = e.getType();

        ApplicationContextUtils.getBeansOfType(FieldFilterComponent.class).stream().filter(v -> v.getToClass() == null
                || v.getToClass().length == 0 || Arrays.asList(v.getToClass()).contains(type)).forEach(w -> {
            Component component = w.getComponent(e);
            if (component != null) {
                putBinding(e, component);
                ReflectionZKUtil.widthComponent(component);
                campo.appendChild(component);
                loadReglon(renglon, labeldiv, label, campo);
            }
        });

        return renglon;
    }

    public Component putBinding(Field key, Component value) {
        return binding.put(key, value);
    }

    public void loadReglon(Div renglon, Div labeldiv, Label label, Div campo) {
        labeldiv.appendChild(label);
        renglon.appendChild(labeldiv);
        renglon.setStyle("margin-top:10px;margin-buttom:10px;");
        renglon.setZclass(reglonClass);
        labeldiv.setZclass("col-sm-12");
        campo.setZclass("col-sm-12");
        renglon.appendChild(campo);
    }
}
