package com.gitlab.pedrioko.core.view.viewers;

import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.AplicateAllClass;
import com.gitlab.pedrioko.core.view.enums.CrudAction;
import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CrudMenuContext extends Menupopup {

    private final Class klass;
    private List<Action> actions = new ArrayList<>();
    private Object valueSelect;

    public CrudMenuContext(Class klass, List<Action> actions) {
        this.klass = klass;
        this.setActions(actions);
    }

    private void init() {
        this.getChildren().clear();
        List<Action> collect = this.actions.stream().filter(Action::MenuSupported).collect(toList());
        List<Action> collect1 = collect.stream().filter(e -> CollectionUtils.containsAny(e.getAplicateClass(), Arrays.asList(CrudAction.class, klass, AplicateAllClass.class))).collect(toList());
        collect1.forEach(e -> {
            Menuitem menuitem = new Menuitem();
            menuitem.setIconSclass(e.getIcon());
            menuitem.setLabel(e.getLabel());
            menuitem.setParent(this);
            menuitem.setValue(e.getLabel());
            menuitem.addEventListener(Events.ON_CLICK, (w) -> e.actionPerform(new CrudActionEvent(valueSelect)));
            appendChild(menuitem);
        });
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
        this.init();
    }

    public void open(Component c, String position, Object data) {
        valueSelect = data;
        open(c, position);
    }
}
