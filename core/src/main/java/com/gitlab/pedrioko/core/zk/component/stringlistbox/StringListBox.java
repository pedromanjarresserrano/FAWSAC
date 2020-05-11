package com.gitlab.pedrioko.core.zk.component.stringlistbox;

import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.forms.CustomForm;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import lombok.Data;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public @Data
class StringListBox extends Div {
    protected Listbox lb = new Listbox();
    private List<String> value = new ArrayList<>();

    public StringListBox(String headername) {
        Listhead columns = new Listhead();
        columns.setParent(lb);
        Listheader column = new Listheader();
        column.setLabel(ReflectionZKUtil.getLabel(headername));
        column.setParent(columns);
        column.setHflex("max");
        lb.setWidth("100%");
        appendChild(lb);
        Button button = new Button();
        button.setLabel(ReflectionZKUtil.getLabel("agregar"));
        button.setClass("push-right btn btn-primary");
        button.addEventListener(Events.ON_CLICK, e -> {
            CustomForm form = new CustomForm(String.class, new LinkedHashMap<>());
            form.addField(ReflectionZKUtil.getLabel("Value"), Textbox.class);
            Textbox label = (Textbox) form.getComponentField("Value");

            form.addAction(ReflectionZKUtil.getLabel("agregar"), "fa fa-plus", e1 -> {
                String load = label.getValue();
                if (load != null && !load.isEmpty()) {
                    form.detach();
                    addValue(load);
                    ZKUtil.showMessage(ReflectionZKUtil.getLabel("userbasicform.guardar"), MessageType.SUCCESS);
                } else {
                    Messagebox.show(Labels.getLabel("valorinvalido"));
                }

            });
            form.addAction(ReflectionZKUtil.getLabel("cancelar"), "fa fa-ban", "btn btn-danger", e1 -> form.detach());
            form.setTitle(ReflectionZKUtil.getLabel("agregar"));
            form.setClass("col-sm-12 col-md-6 col-lg-6");
            ZKUtil.showDialogWindow(form);
        });
        appendChild(button);
        button = new Button();
        button.setLabel(ReflectionZKUtil.getLabel("remove"));
        button.setClass("push-right btn btn-danger");
        button.addEventListener(Events.ON_CLICK, e -> {
            removeValue();
        });
        appendChild(button);
    }

    public void addValue(String value) {
        if (value != null && !value.isEmpty()) {
            Listcell cell = new Listcell();
            Label label = new Label();
            label.setValue(value);
            cell.appendChild(label);
            Listitem row = new Listitem();
            row.appendChild(cell);
            row.setValue(value);
            lb.appendChild(row);
            this.value.add(value);
        }
    }

    public void removeValue() {
        Listitem selectedItem = lb.getSelectedItem();
        if (selectedItem == null) {
            ZKUtil.showMessage(ReflectionZKUtil.getLabel("seleccione"), MessageType.WARNING);
        } else {
            lb.removeChild(selectedItem);
            value.remove(selectedItem.getValue());
        }
    }

    public <T> T getValue() {
        return (T) value;
    }

    public void setValue(ArrayList<String> t) {
        t.forEach(this::addValue);
    }

    public <T> void setValue(T t) {
        ((List<String>) t).forEach(this::addValue);
    }
}
