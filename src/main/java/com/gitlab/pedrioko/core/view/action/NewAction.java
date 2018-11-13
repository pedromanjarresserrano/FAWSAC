package com.gitlab.pedrioko.core.view.action;

import com.gitlab.pedrioko.core.lang.annotation.ToolAction;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.CrudAction;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.forms.EntityForm;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;

import java.util.Arrays;
import java.util.List;

@ToolAction
public class NewAction implements Action {

    @Override
    public String getIcon() {
        return "fas fa-plus";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {
        EntityForm formUtilsnew = new EntityForm(event.getCrudViewParent().getTypeClass());
        formUtilsnew.addAction(ApplicationContextUtils.getBean(SaveAction.class), event);
        formUtilsnew.addAction(ApplicationContextUtils.getBean(CancelAction.class), event);
        formUtilsnew.setEstado(FormStates.CREATE);
        event.getCrudViewParent().setContent(formUtilsnew);
    }

    @Override
    public List<?> getAplicateClass() {
        return Arrays.asList(CrudAction.class);
    }

    @Override
    public String getLabel() {
        return "";
    }

    @Override
    public String getClasses() {
        return "btn-warning";
    }

    @Override
    public FormStates getFormState() {
        return FormStates.CREATE;
    }

    @Override
    public Integer position() {
        return 1;
    }

    @Override
    public String getColor() {
        return "#27ae60";
    }

    @Override
    public int getGroup() {
        return 0;
    }

    @Override
    public String getTooltipText() {
        return ReflectionZKUtil.getLabel("nuevo");
    }

    @Override
    public boolean isDefault() {
        return true;
    }
}
