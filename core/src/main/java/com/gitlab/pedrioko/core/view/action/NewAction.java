package com.gitlab.pedrioko.core.view.action;

import com.gitlab.pedrioko.core.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.CrudAction;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@org.springframework.stereotype.Component
public class NewAction implements Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewAction.class);

    @Override
    public String getIcon() {
        return "fas fa-plus";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {
        HashMap<Object, Object> arg = new HashMap<>();

        CrudView crudViewParent = (CrudView) event.getCrudViewParent();
        Class<?> typeClass = crudViewParent.getTypeClass();
        arg.put("value", ReflectionJavaUtil.getNewInstace(typeClass));
        arg.put("event-crud", event);
        arg.put("estado-form", FormStates.CREATE);
        Component component = null;
        try {
            component = Executions.createComponents("~./zul/forms/form" + typeClass.getSimpleName() + ".zul", null, arg);
        } catch (Exception e) {
            LOGGER.info("CUSTOM ENTITY FORM PAGE NOT FOUND....");
            LOGGER.info("USING DEFAULT ENTITY FORM  PAGE ");
        }
        if (component == null) component = Executions.createComponents("~./zul/forms/form.zul", null, arg);
        crudViewParent.setContent(component);
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
