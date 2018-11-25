package com.gitlab.pedrioko.core.view.viewers;

import com.gitlab.pedrioko.core.view.api.CrudDisplayTable;
import com.gitlab.pedrioko.core.view.api.ListCellCustomizer;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import org.zkoss.zul.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CrudTable extends Listbox implements CrudDisplayTable {

    private static final long serialVersionUID = 1L;
    private final transient List listitems = new ArrayList();
    private transient List<Field> fieldsIncludingSuperclasses;
    private Listhead columns;
    private Class<?> klass;
    private List<String> fields;

    CrudTable(Class<?> klass) {
        super();
        init(klass);
        loadfields(klass);
    }

    CrudTable(Class<?> klass, List<Class<?>> all) {
        super();
        listitems.clear();
        listitems.addAll(all);
        init(klass);
        loadfields(klass);
    }

    CrudTable(List<String> fields, Class<?> klass) {
        super();
        init(klass);
        this.fields = fields;
        loadfields(klass);
    }

    private void init(Class<?> klass) {
        columns = new Listhead();
        columns.setParent(this);
        this.klass = klass;
        if (ReflectionZKUtil.isInEventListener()) {
            setAutopaging(true);
            setMold("paging");
            setPageSize(25);
        }
        setVflex("1");
        setHflex("1");
        setStyle("height:98;");
    }

    private void loadHeads() {
        fieldsIncludingSuperclasses.forEach(e -> {
            Listheader column = new Listheader();
            configColumn(e, column);
        });
    }

    private void configColumn(Field e, Listheader column) {
        column.setLabel(ReflectionZKUtil.getLabel(e.getName()));
        column.setParent(columns);
        column.setHflex("min");
    }

    private void loadItems() {
        getItems().clear();
        if (listitems != null)
            listitems.forEach(e -> {
                Listitem row = new Listitem();
                for (Field w : fieldsIncludingSuperclasses) {
                    Object runGetter = ReflectionJavaUtil.getValueFieldObject(w.getName(), e);
                    Listcell cell = new Listcell();
                    Optional<ListCellCustomizer> cellCustomizer = ApplicationContextUtils.getBeans(ListCellCustomizer.class).stream().filter(c -> c.getToClass().contains(w.getType())).findFirst();
                    if (cellCustomizer.isPresent()) {
                        cell.appendChild(cellCustomizer.get().getCompònent(runGetter));
                    } else {
                        Label label = new Label();
                        label.setValue(runGetter != null ? ReflectionZKUtil.getLabel(runGetter.toString()) : "Null");
                        cell.appendChild(label);
                    }
                    row.appendChild(cell);
                }
                row.setValue(e);
                appendChild(row);
            });
    }

    private void loadfields(Class<?> klass) {
        fieldsIncludingSuperclasses = ReflectionJavaUtil.getFields(klass);
        if (fields != null && !fields.isEmpty()) {
            List<Field> filter = fieldsIncludingSuperclasses.stream().filter(e -> fields.contains(e.getName()))
                    .collect(Collectors.toList());
            fieldsIncludingSuperclasses = filter;
        }
        loadHeads();
    }

    @Override
    public <T> T getSelectedValue() {
        return getSelectedItem() == null ? null : getSelectedItem().getValue();
    }

    @Override
    public List getValue() {
        return listitems;
    }

    @Override
    public void setValue(List<?> all) {
        listitems.clear();
        listitems.addAll(all);
        loadItems();
    }

    @Override
    public void clearSelection() {
        super.clearSelection();
    }

    @Override
    public void update() {
        loadItems();

    }


}
