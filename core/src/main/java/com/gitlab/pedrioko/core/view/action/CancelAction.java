package com.gitlab.pedrioko.core.view.action;

import com.gitlab.pedrioko.core.lang.annotation.ToolAction;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.forms.Form;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;

import java.util.Arrays;
import java.util.List;

@ToolAction
public class CancelAction implements Action {

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("cancelar");
    }

    @Override
    public String getIcon() {
        return "z-icon-ban";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {
        ((CrudView) event.getCrudViewParent()).previousState();
    }

    @Override
    public List<?> getAplicateClass() {
        return Arrays.asList(Form.class);
    }

    @Override
    public String getClasses() {
        return "order-12 btn-danger";
    }

    @Override
    public FormStates getFormState() {
        return null;
    }

    @Override
    public Integer position() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getColor() {
        return null;
    }

    @Override
    public int getGroup() {
        return 0;
    }

    @Override
    public String getTooltipText() {
        return null;
    }
}
