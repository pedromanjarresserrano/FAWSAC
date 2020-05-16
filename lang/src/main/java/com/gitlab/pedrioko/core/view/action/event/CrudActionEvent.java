package com.gitlab.pedrioko.core.view.action.event;

import com.gitlab.pedrioko.core.view.api.Valuable;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import lombok.Data;

public @Data
class CrudActionEvent {
    private transient Object value;
    private Object crudViewParent;
    private FormStates formstate;
    private Class type;
    private Valuable source;

    public CrudActionEvent() {
    }

    public CrudActionEvent(Object value) {
        this.value = value;
    }

    public FormStates getFormstate() {
        return formstate;
    }

}

