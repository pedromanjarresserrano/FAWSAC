package com.gitlab.pedrioko.core.view.action.event;

import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.forms.Form;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import lombok.Data;
import org.zkoss.zul.Tabpanel;

public @Data
class CrudActionEvent {
    private Object value;
    private Tabpanel tabpanel;
    private CrudView crudViewParent;
    private FormStates formstate;
    private Form source;

    public CrudActionEvent() {
    }

    public CrudActionEvent(Object value) {
        this.value = value;
    }

    public FormStates getFormstate() {
        return formstate;
    }
}
