package com.gitlab.pedrioko.core.view.action;

import com.gitlab.pedrioko.core.lang.annotation.ToolAction;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.CrudAction;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.zkoss.zk.ui.Executions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@ToolAction
@Order(0)
public class ViewAction implements Action {
    @Autowired
    private CrudService crudService;

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
            HashMap<Object, Object> arg = new HashMap<>();
            arg.put("value", crudService.refresh(value));
            arg.put("event-crud", event);
            arg.put("estado-form", FormStates.READ);

           ZKUtil.showDialogWindow(Executions.createComponents("~./zul/form.zul", null, arg));
        }
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

    @Override
    public boolean isDefault() {
        return true;
    }
}
