package com.gitlab.pedrioko.core.view.action.api;

import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.FormStates;

import java.util.List;

public interface Action {

    default boolean isDefault() {
        return false;
    }

    public String getLabel();

    public String getTooltipText();

    public String getIcon();

    public void actionPerform(CrudActionEvent event);

    @SuppressWarnings("rawtypes")
    public List<?> getAplicateClass();

    public String getClasses();

    public FormStates getFormState();

    public Integer position();

    public String getColor();

    public int getGroup();

    default boolean visibleByDefault() {
        return true;
    }

    default boolean MenuSupported() {
        return false;
    }

    default boolean showLabel() {
        return false;
    }
}
