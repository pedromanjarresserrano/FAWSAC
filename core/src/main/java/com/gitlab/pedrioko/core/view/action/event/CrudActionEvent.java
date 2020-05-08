package com.gitlab.pedrioko.core.view.action.event;

import com.gitlab.pedrioko.core.view.api.Valuable;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import lombok.Data;
import org.zkoss.zul.Tabpanel;

public @Data
class CrudActionEvent {
    private Object value;
    private CrudView crudViewParent;
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

