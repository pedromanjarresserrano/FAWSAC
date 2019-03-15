package com.gitlab.pedrioko.core.view.action;

import com.gitlab.pedrioko.core.lang.annotation.ToolAction;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.CrudAction;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import org.zkoss.zk.ui.Executions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@ToolAction
public class NewAction implements Action {

    @Override
    public String getIcon() {
        return "fas fa-plus";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {
        HashMap<Object, Object> arg = new HashMap<>();
        try {
            arg.put("value", ReflectionJavaUtil.getNewInstace(event.getCrudViewParent().getTypeClass()));
            arg.put("event-crud", event);
            arg.put("estado-form", FormStates.CREATE);
        } catch (Exception e) {
        }
        event.getCrudViewParent().setContent(Executions.createComponents("~./zul/form.zul", null, arg));
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
