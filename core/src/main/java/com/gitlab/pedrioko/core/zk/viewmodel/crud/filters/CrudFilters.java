package com.gitlab.pedrioko.core.zk.viewmodel.crud.filters;

import com.gitlab.pedrioko.core.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.FieldFilterComponent;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.viewers.crud.controllers.CrudController;
import com.gitlab.pedrioko.core.view.viewers.crud.controllers.model.OrderBY;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import javax.persistence.Id;
import javax.persistence.Version;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CrudFilters {

    private final transient Map<Field, Component> binding = new LinkedHashMap<>();
    private CrudController crudController;
    private Class<?> klass;
    private List<String> fieldsfilters;
    private Map<String, Component> filters = new LinkedHashMap<>();
    private List<Field> listfield;
    private String fieldname;
    private OrderBY orderBY;

    @Init
    private void init() {
        Map<?, ?> arg = (Map<?, ?>) Executions.getCurrent().getArg();
        crudController = (CrudController) arg.get("crud-controller");
        klass = crudController.getTypeClass();
        listfield = ReflectionJavaUtil.getFields(getKlass()).stream()
                .filter(e -> !e.isAnnotationPresent(Version.class) && !e.getName().equalsIgnoreCase("serialVersionUID")
                        && !e.isAnnotationPresent(Id.class)
                        && (fieldsfilters == null || fieldsfilters.isEmpty() || fieldsfilters.contains(e.getName())))
                .collect(Collectors.toList());
        listfield.forEach(e -> fieldToUiField(e));

    }

    private void fieldToUiField(Field e) {
        Class<?> type = e.getType();
        ApplicationContextUtils.getBeansOfType(FieldFilterComponent.class).stream().filter(v -> v.getToClass() == null
                || v.getToClass().length == 0 || Arrays.asList(v.getToClass()).contains(type) || Arrays.stream(v.getToClass()).anyMatch(t -> t.isAssignableFrom(type))).forEach(w -> {
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

    public Map<String, Component> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Component> filters) {
        this.filters = filters;
    }

    public List<Field> getListfield() {
        return listfield;
    }

    public List<OrderBY> getOrderByList() {
        return Arrays.asList(OrderBY.values());
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
        this.crudController.setOrderField(fieldname);
    }

    public OrderBY getOrderBY() {
        return orderBY;
    }

    public void setOrderBY(OrderBY orderBY) {
        this.orderBY = orderBY;
        this.crudController.setOrderBY(orderBY);
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
        crudController.setPage(0);
    }

    @Command
    public void clearFilters() {
        binding.forEach((k, v) -> {
            ReflectionZKUtil.setValueComponent(v, null);
        });
        crudController.clearParams();
        crudController.setOrderField("");
        crudController.doQuery();
    }
}
