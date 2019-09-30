package com.gitlab.pedrioko.core.zk.viewmodel;

import com.gitlab.pedrioko.core.view.api.Valuable;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.zk.component.listbox.ListBox;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ListActionsVM implements Valuable {


    private Object valueList;
    @Wire("#listaccessform")
    private ListBox list;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        list.addEventListener(Events.ON_CHANGE, (event) -> {
            System.out.println("changed");
        });
    }

    @Init
    public void init() {

    }

    @Override
    public void setValueForm(Object obj) {

    }

    @Override
    public FormStates getEstado() {
        return null;
    }

    @Override
    public void setEstado(FormStates update) {

    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {

    }

    public Object getValueList() {
        return valueList;
    }

    public void setValueList(Object valueList) {
        this.valueList = valueList;
    }
}
