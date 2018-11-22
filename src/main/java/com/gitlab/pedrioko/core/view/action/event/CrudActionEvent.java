package com.gitlab.pedrioko.core.view.action.event;

import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.viewers.CrudView;
import lombok.Data;
import org.zkoss.zul.Tabpanel;

public @Data
class CrudActionEvent {
    private Object value;
    private Tabpanel tabpanel;
    private CrudView crudViewParent;
    private FormStates formstate;

    public CrudActionEvent() {
    }

    public CrudActionEvent(Object value) {
        this.value = value;
    }
}
