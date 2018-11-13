package com.gitlab.pedrioko.core.view.action;

import com.gitlab.pedrioko.core.lang.annotation.ToolAction;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.enums.SubCrudView;
import com.gitlab.pedrioko.core.view.forms.CustomForm;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.ArraysUtil;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.core.view.viewers.CrudView;
import com.gitlab.pedrioko.services.CrudService;
import org.springframework.core.annotation.Order;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@ToolAction
@Order(0)
public class AddAction implements Action {

    private static final String AGREGAR = "Agregar";

    @Override
    public String getIcon() {
        return "fa  fa-plus-square";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {

        CrudView crudViewParent = event.getCrudViewParent();
        Class<?> typeClass = crudViewParent.getTypeClass();
        CustomForm form = new CustomForm(typeClass, new LinkedHashMap<>());
        form.addField(AGREGAR, Combobox.class);
        Combobox combobox = (Combobox) form.getComponentField(AGREGAR);
        List<? extends Object> all = ApplicationContextUtils.getBean(CrudService.class).getAll(typeClass);
        ArrayList<?> value = crudViewParent.getValue();
        ArraysUtil.removeDuplicates(typeClass, all, value);
        all.forEach(e -> {
            Comboitem comboitem = new Comboitem();
            comboitem.setLabel(e.toString());
            comboitem.setValue(e);
            combobox.getItems().add(comboitem);
        });
        form.addAction(ReflectionZKUtil.getLabel("agregar"), "fa fa-plus", e -> {
            crudViewParent.addValue(combobox.getSelectedItem().getValue());
            form.detach();
            ZKUtil.showMessage(ReflectionZKUtil.getLabel("userbasicform.guardar"), MessageType.SUCCESS);
        });
        form.addAction(ReflectionZKUtil.getLabel("cancelar"), "fa fa-ban", "btn btn-danger", e -> form.detach());
        form.setTitle(AGREGAR);
        form.setClass("col-sm-12 col-md-6 col-lg-6");
        ZKUtil.showDialogWindow(form);

    }

    @Override
    public List<?> getAplicateClass() {
        return Arrays.asList(SubCrudView.class);
    }

    @Override
    public String getLabel() {
        return "";
    }

    @Override
    public String getClasses() {
        return "btn-info";
    }

    @Override
    public FormStates getFormState() {
        return FormStates.READ;
    }

    @Override
    public Integer position() {
        return 0;
    }

    @Override
    public String getColor() {
        return "#01419f";
    }

    @Override
    public int getGroup() {
        return 2;
    }

    @Override
    public String getTooltipText() {
        return ReflectionZKUtil.getLabel("agregar");
    }

}
