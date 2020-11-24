package com.gitlab.pedrioko.core.view.action;

import org.springframework.stereotype.Component;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import com.gitlab.pedrioko.domain.Usuario;
import com.gitlab.pedrioko.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.zkoss.zul.Messagebox;

import java.util.Arrays;
import java.util.List;

@Component
@Order(0)
public class RemoveProfileAction implements Action {

    @Autowired
    private CrudService crudService;

    @Override
    public String getIcon() {
        return "fa  fa-user-times";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {

        Object value = event.getValue();
        if (value == null) {
            ZKUtil.showMessage(ReflectionZKUtil.getLabel("seleccione"), MessageType.WARNING);
        } else {
            Messagebox.show(ReflectionZKUtil.getLabel("estaseguro"), ReflectionZKUtil.getLabel("warning"), Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, event1 -> {
                if (event1.getName().equalsIgnoreCase(Messagebox.ON_OK)) {
                    ((Usuario) value).getUserprofiles().clear();
                    crudService.saveOrUpdate(value);
                    ((CrudView) event.getCrudViewParent()).getCrudController().doQuery();
                    ZKUtil.showMessage(ReflectionZKUtil.getLabel("removed"), MessageType.WARNING);
                }
            });
        }

    }

    @Override
    public List<?> getAplicateClass() {
        return Arrays.asList(Usuario.class);
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
        return "#9b59b6";
    }

    @Override
    public int getGroup() {
        return 2;
    }

    @Override
    public String getTooltipText() {
        return ReflectionZKUtil.getLabel("removeperfiles");
    }

}
