package com.gitlab.pedrioko.core.view.action;

import com.gitlab.pedrioko.core.lang.annotation.ToolAction;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.CrudAction;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import com.gitlab.pedrioko.services.CrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@ToolAction
public class SearchAction implements Action {

    @Autowired
    private CrudService crudservice;

    @Override
    public String getLabel() {
        return "Filtrar";
    }

    @Override
    public String getIcon() {
        return "fa fa-filter";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {
        ((CrudView)event.getCrudViewParent()).openFilters();
    }

    @Override
    public List<?> getAplicateClass() {
        return Arrays.asList(CrudAction.class);
    }

    @Override
    public String getClasses() {
        return "btn-default";
    }

    @Override
    public FormStates getFormState() {
        return null;
    }

    @Override
    public Integer position() {
        return 4;
    }

    @Override
    public String getColor() {
        return "#B2B2B2";
    }

    @Override
    public int getGroup() {
        return 0;
    }

    @Override
    public String getTooltipText() {
        return ReflectionZKUtil.getLabel("filtrar");
    }

    @Override
    public boolean showLabel() {
        return false;
    }

    @Override
    public boolean MenuSupported() {
        return true;
    }
}
