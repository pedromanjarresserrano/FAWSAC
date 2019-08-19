package com.gitlab.pedrioko.core.view.action;

import com.gitlab.pedrioko.core.lang.ProviderAccess;
import com.gitlab.pedrioko.core.lang.annotation.ToolAction;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.forms.AccessForm;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;

import java.util.Arrays;
import java.util.List;

@ToolAction
public class NewAccessAction implements Action {

    @Override
    public String getIcon() {
        return "fas fa-plus";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {
        AccessForm form = new AccessForm();
        form.addAction(ApplicationContextUtils.getBean(SaveAction.class), event);
        form.addAction(ApplicationContextUtils.getBean(CancelAction.class), event);
        form.setEstado(FormStates.CREATE);
        event.getCrudViewParent().setContent(form);
    }

    @Override
    public List<?> getAplicateClass() {
        return Arrays.asList(ProviderAccess.class);
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
}
