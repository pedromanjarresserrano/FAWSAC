package com.gitlab.pedrioko.core.view.forms;

import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.ArraysUtil;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import com.gitlab.pedrioko.core.zk.component.chosenbox.ChosenBoxImage;
import com.gitlab.pedrioko.core.zk.component.chosenbox.ChosenFileEntityBox;
import com.gitlab.pedrioko.services.CrudService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;


public class AddForm extends CustomForm {
    private static final String AGREGAR = "Agregar";

    private static final long serialVersionUID = 1L;
    private String fieldname;

    public AddForm(Class klass, String field) {
        super(klass, new LinkedHashMap<>());
        this.addField(field, ChosenFileEntityBox.class);
    }


    public AddForm(String fieldname, Class<?> typeClass, Class<?> componentClass, EventListener<? extends Event> addevent, EventListener<? extends Event> cancelevent) {
        super(typeClass, new LinkedHashMap<>());
        build(fieldname, typeClass, componentClass, false, null, addevent, cancelevent);
    }

    public AddForm(String fieldname, Class<?> typeClass, Class<?> componentClass, EventListener<? extends Event> addevent) {
        super(typeClass, new LinkedHashMap<>());
        build(fieldname, typeClass, componentClass, false, null, addevent, null);
    }

    public AddForm(String fieldname, Class<?> typeClass, Class<?> componentClass, boolean removeDuplicates, CrudView crudView, EventListener<? extends Event> addevent, EventListener<? extends Event> cancelevent) {
        super(typeClass, new LinkedHashMap<>());
        build(fieldname, typeClass, componentClass, removeDuplicates, crudView.getValue(), addevent, cancelevent);
    }

    public AddForm(String fieldname, Class<?> typeClass, Class<?> componentClass, boolean removeDuplicates, CrudView crudView, EventListener<? extends Event> addevent) {
        super(typeClass, new LinkedHashMap<>());
        build(fieldname, typeClass, componentClass, removeDuplicates, crudView.getValue(), addevent, null);
    }

    public AddForm(String fieldname, Class<?> typeClass, Class<?> componentClass, boolean removeDuplicates, List<?> value, EventListener<? extends Event> addevent) {
        super(typeClass, new LinkedHashMap<>());
        build(fieldname, typeClass, componentClass, removeDuplicates, value, addevent, null);
    }

    public AddForm(String fieldname, Class<?> typeClass, Class<?> componentClass, boolean removeDuplicates, List<?> value, EventListener<? extends Event> addevent, EventListener<? extends Event> cancelevent) {
        super(typeClass, new LinkedHashMap<>());
        build(fieldname, typeClass, componentClass, removeDuplicates, value, addevent, cancelevent);

    }

    private void build(String fieldname, Class<?> typeClass, Class<?> componentClass, boolean removeDuplicates, List<?> value, EventListener<? extends Event> addevent, EventListener<? extends Event> cancelevent) {
        this.fieldname = fieldname;
        if (fieldname == null || this.fieldname.isEmpty())
            this.addField(AGREGAR, componentClass);
        else
            this.addField(fieldname, componentClass);
        this.setTitle(AGREGAR);
        List<? extends Object> all = new LinkedList<>(ApplicationContextUtils.getBean(CrudService.class).getAllOrder(typeClass));
        if (removeDuplicates && value != null) {
            ArraysUtil.removeDuplicates(typeClass, all, value);
        }

        Component componentField = fieldname == null || fieldname.isEmpty() ? this.getComponentField(AGREGAR) : this.getComponentField(fieldname);

        //this.putBinding((fieldname == null || fieldname.isEmpty() )? AGREGAR:this.fieldname,componentField);

        if (componentClass == ChosenBoxImage.class) {
            ((ChosenBoxImage) componentField).setModel(all);
        }
        if (componentClass == Combobox.class) {
            all.forEach(e -> {
                Comboitem comboitem = new Comboitem();
                comboitem.setLabel(e.toString());
                comboitem.setValue(e);
                ((Combobox) componentField).getItems().add(comboitem);
            });
        }
        if (fieldname == null || fieldname.isEmpty())
            this.getRenglon(AGREGAR).setZclass("ats-addform");
        else
            this.getRenglon(fieldname).setZclass("ats-addform");
        this.addAction(ReflectionZKUtil.getLabel("agregar"), "fa fa-plus", "ats-addform-add", addevent);
        if (cancelevent != null)
            this.addAction(ReflectionZKUtil.getLabel("cancelar"), "fa fa-ban", "ats-addform-cancel", cancelevent);
        else
            this.addAction(ReflectionZKUtil.getLabel("cancelar"), "fa fa-ban", "ats-addform-cancel", e -> this.detach());
    }

    @Override
    public void addAction(String labelaction, String icon, String classes, EventListener event) {
        Button btn = new Button();
        btn.setLabel(labelaction);
        btn.setIconSclass(icon);
        btn.setClass("btn-action " + classes + (ZKUtil.isMobile() ? " col-sm-12 " : ""));

        btn.addEventListener(Events.ON_CLICK, e -> {
            Component componentField = this.getComponentField(fieldname);
            Object value = null;
            if (componentField.getClass() == ChosenBoxImage.class) {
                value = ((ChosenBoxImage) componentField).getValue();
            }
            if (componentField.getClass() == Combobox.class) {
                value = ((Combobox) componentField).getSelectedItem().getValue();
            }
            event.onEvent(new Event("", this, value));
        });
        getActions().appendChild(btn);
    }
}
