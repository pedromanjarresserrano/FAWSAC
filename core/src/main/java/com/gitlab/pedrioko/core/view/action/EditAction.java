package com.gitlab.pedrioko.core.view.action;

import com.gitlab.pedrioko.core.lang.annotation.ToolAction;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.CrudAction;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@ToolAction
public class EditAction implements Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditAction.class);

    @Override
    public String getIcon() {
        return "fas fa-edit";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {
        Object value = event.getValue();
        if (value == null) {
            ZKUtil.showMessage(ReflectionZKUtil.getLabel("seleccione"), MessageType.WARNING);
        } else {
            HashMap<Object, Object> arg = new HashMap<>();
            Class<?> typeClass = event.getCrudViewParent().getTypeClass();
            arg.put("value", value);
            arg.put("event-crud", event);
            arg.put("estado-form", FormStates.UPDATE);
            Component component = null;
            try {
                component = Executions.createComponents("~./zul/forms/form" + typeClass.getSimpleName() + ".zul", null, arg);
            } catch (Exception e) {
                LOGGER.info("CUSTOM ENTITY FORM PAGE NOT FOUND....");
                LOGGER.info("USING DEFAULT ENTITY FORM  PAGE ");
            }
            if (component == null) component = Executions.createComponents("~./zul/forms/form.zul", null, arg);
            event.getCrudViewParent().setContent(component);
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
        return "btn-primary";
    }

    @Override
    public FormStates getFormState() {
        return FormStates.UPDATE;
    }

    @Override
    public Integer position() {
        return 2;
    }

    @Override
    public String getColor() {
        return "#f1c40f";
    }

    @Override
    public int getGroup() {
        return 0;
    }

    @Override
    public String getTooltipText() {
        return ReflectionZKUtil.getLabel("editar");
    }

    @Override
    public boolean isDefault() {
        return true;
    }
}
