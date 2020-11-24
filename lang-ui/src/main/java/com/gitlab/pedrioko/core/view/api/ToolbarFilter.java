package com.gitlab.pedrioko.core.view.api;

import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import org.zkoss.zk.ui.Component;

import java.util.List;

public interface ToolbarFilter {

    public String getLabel();

    public String getField();

    public String getTooltipText();

    Component getComponent();

    public void onChangeAction(CrudView crudView);

    public List<Class<?>> getAplicateClass();

    public Integer position();
}
