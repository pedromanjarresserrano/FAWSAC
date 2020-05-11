package com.gitlab.pedrioko.core.view.action;

import com.gitlab.pedrioko.core.lang.ProviderAccess;
import com.gitlab.pedrioko.core.lang.annotation.ToolAction;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.forms.AccessForm;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.ZKUtil;

import java.util.Arrays;
import java.util.List;

@ToolAction
public class ViewAccessAction implements Action {

    @Override
    public String getIcon() {
        return "fas fa-info-circle";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {
        Object value = event.getValue();
        if (value == null) {
            ZKUtil.showMessage(ReflectionZKUtil.getLabel("seleccione"), MessageType.WARNING);
        } else {
            AccessForm form = new AccessForm();
            form.setValueForm(event.getValue());
            form.addAction(ApplicationContextUtils.getBean(CancelAction.class), event);
            form.setEstado(FormStates.READ);
            event.getCrudViewParent().setContent(form);
        }
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
        return FormStates.READ;
    }

    @Override
    public Integer position() {
        return 0;
    }

    @Override
    public String getColor() {
        return "#3498db";
    }

    @Override
    public int getGroup() {
        return 0;
    }

    @Override
    public String getTooltipText() {
        return ReflectionZKUtil.getLabel("ver");
    }
}
