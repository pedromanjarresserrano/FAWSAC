package com.gitlab.pedrioko.core.zk.viewmodel.crud.filters;

import com.gitlab.pedrioko.core.view.api.FieldFilterComponent;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.viewers.crud.controllers.CrudController;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import javax.persistence.Id;
import javax.persistence.Version;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class CrudFilters {

    private CrudController crudController;
    private Class<?> klass;
    private final Random random = new Random();
    private List<String> fieldsfilters;
    private Map<String, Component> filters = new LinkedHashMap<>();
    private final transient Map<Field, Component> binding = new LinkedHashMap<>();

    @Init
    private void init() {
        crudController = (CrudController) Executions.getCurrent().getArg().get("crud-controller");
        klass = crudController.getTypeClass();
        List<Field> listfield = ReflectionJavaUtil.getFields(getKlass()).stream()
                .filter(e -> !e.isAnnotationPresent(Version.class) && !e.getName().equalsIgnoreCase("serialVersionUID")
                        && !e.isAnnotationPresent(Id.class)
                        && (fieldsfilters == null || fieldsfilters.isEmpty() || fieldsfilters.contains(e.getName())))
                .collect(Collectors.toList());
        listfield.forEach(e -> fieldToUiField(e));

    }

    private void fieldToUiField(Field e) {
        Class<?> type = e.getType();
        ApplicationContextUtils.getBeansOfType(FieldFilterComponent.class).stream().filter(v -> v.getToClass() == null
                || v.getToClass().length == 0 || Arrays.asList(v.getToClass()).contains(type)).forEach(w -> {
            Component component = w.getComponent(e);
            if (component != null) {
                putBinding(e, component);
                filters.put(ReflectionZKUtil.getLabel(e), component);
            }
        });
    }

    private Component putBinding(Field key, Component value) {
        return binding.put(key, value);
    }

    public Class<?> getKlass() {
        return klass;
    }

    public void setKlass(Class<?> klass) {
        this.klass = klass;
    }

    public List<String> getFieldsfilters() {
        return fieldsfilters;
    }

    public void setFieldsfilters(List<String> fieldsfilters) {
        this.fieldsfilters = fieldsfilters;
    }

    public Map<String, Component> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Component> filters) {
        this.filters = filters;
    }

    @Command
    public void search() {
        crudController.clearParams();
        binding.forEach((k, v) -> {
            Object valueComponent = ReflectionZKUtil.getValueComponent(v);
            if (valueComponent != null)
                if (valueComponent instanceof String) {
                    if (!((String) valueComponent).isEmpty())
                        crudController.put(k.getName(), valueComponent);
                } else crudController.put(k.getName(), valueComponent);

        });
        crudController.doQuery();
        BindUtils.postGlobalCommand(null, null, "refresh", null);
    }

    @Command
    public void clearFilters() {
        binding.forEach((k, v) -> {
            ReflectionZKUtil.setValueComponent(v, null);
        });
        crudController.clearParams();
        crudController.doQuery();
        BindUtils.postGlobalCommand(null, null, "refresh", null);
    }
}
