package com.gitlab.pedrioko.core.view.action.api;

import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.FormStates;

import java.util.List;

public interface Action {

    default boolean isDefault() {
        return false;
    }

    public String getLabel();

    default String getTooltipText() {
        return "";
    }

    public String getIcon();

    public void actionPerform(CrudActionEvent event);

    @SuppressWarnings("rawtypes")
    public List<?> getAplicateClass();

    default String getClasses() {
        return "";
    }

    default FormStates getFormState() {
        return FormStates.READ;
    }

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
