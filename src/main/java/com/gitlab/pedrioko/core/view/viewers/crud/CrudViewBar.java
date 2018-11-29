package com.gitlab.pedrioko.core.view.viewers.crud;

import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.api.CrudDisplayTable;
import com.gitlab.pedrioko.core.view.enums.*;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.PropertiesUtil;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.core.zk.component.ActionButton;
import com.gitlab.pedrioko.core.zk.component.ActionMobileButton;
import com.gitlab.pedrioko.services.CrudService;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;

import java.util.*;

import static com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil.getLabel;
import static com.gitlab.pedrioko.core.view.util.ApplicationContextUtils.getBean;
import static com.gitlab.pedrioko.core.view.util.ApplicationContextUtils.getBeansOfType;
import static java.util.stream.Collectors.groupingBy;

class CrudViewBar extends Toolbar {

    private static final long serialVersionUID = 1L;
    private final boolean enableCommonActionsClass;
    private final Class<?> klass;
    private @Getter
    final
    List<Component> crudsActions = new ArrayList<>();
    private @Getter
    final
    List<Component> actions = new ArrayList<>();

    private final transient CrudService crudService;
    private Menupopup menupopup;
    private CrudView parent;
    private Menubar mb = null;
    private Hlayout hlayout = new Hlayout();

    CrudViewBar(Class<?> klass, CrudView parent, CrudDisplayTable crudTable) {
        super();
        this.parent = parent;
        if (ZKUtil.isMobile()) {
            mb = new Menubar();
            Menu menu = new Menu();
            mb.appendChild(menu);
            menu.setIconSclass("fas fa-caret-square-down fa-2x");
            menupopup = new Menupopup();
            menu.appendChild(menupopup);
            hlayout.appendChild(mb);
            hlayout.setSpacing("0px");

        }

        enableCommonActionsClass = getBean(PropertiesUtil.class).getEnableCommonActionsClass(klass);
        crudService = getBean(CrudService.class);
        setOrient("horizontal");
        this.klass = klass;
        Map<Integer, List<Action>> listMap = getBeansOfType(Action.class).stream().sorted(Comparator.comparing(Action::position)).collect(groupingBy(Action::getGroup));
        listMap.forEach((k, v) -> {
            v.forEach(e -> {
                Class<?> subCrudViewClass = this.parent.getCrudviewmode() == CrudMode.SUBCRUD ? SubCrudView.class : ApplicationContextUtils.class;
                if (CollectionUtils.containsAny(e.getAplicateClass(), Arrays.asList(CrudAction.class, klass, AplicateAllClass.class, subCrudViewClass))) {
                    Component button;
                    if (ZKUtil.isMobile()) {
                        button = addMenuitemAction(e, parent, crudTable);
                    } else {
                        button = addToolbarAction(e, parent, crudTable);
                    }
                    if (e.isDefault() && k == 0) {
                        crudsActions.add(button);
                    }
                    actions.add(button);
                    button.setVisible(e.visibleByDefault());
                }

            });

            if (!ZKUtil.isMobile())
                appendChild(getSpace());
        });

        if (!enableCommonActionsClass) {
            crudsActions.forEach(e -> e.setVisible(false));
        }

        setStyle("background: white; width:100%;");
        Textbox textbox = new Textbox();
        textbox.setClass("pull-right");
        textbox.setHeight("33px");
        textbox.setPlaceholder(getLabel("buscar"));
        textbox.addEventListener(Events.ON_OK, e -> parent.setValue(crudService.getLike(klass, textbox.getValue())));
        if (ZKUtil.isMobile()) {
            hlayout.appendChild(textbox);
            appendChild(hlayout);
        } else
            appendChild(textbox);
    }

    private Space getSpace() {
        Space separator = new Space();
        separator.setSclass("toolbar-separator");
        return separator;
    }

    private ActionMobileButton addMenuitemAction(Action v, CrudView parent, CrudDisplayTable crudTable) {
        ActionMobileButton mi = new ActionMobileButton();
        mi.setKlass(v.getClass());
        mi.setIconSclass(v.getIcon());
        String label = v.getTooltipText();
        mi.setLabel(label);
        mi.setValue(label);
        CrudActionEvent event = new CrudActionEvent();
      //  event.setTabpanel(parent);
        event.setCrudViewParent(parent);
        mi.addEventListener(Events.ON_CLICK, e -> onClickListener(v, crudTable, event));
        menupopup.appendChild(mi);
        return mi;
    }


    private ActionButton addToolbarAction(Action v, CrudView parent, CrudDisplayTable crudTable) {
        ActionButton btn = new ActionButton();
        btn.setId(klass.getSimpleName() + "-" + v.getClass().getSimpleName() + "-" + UUID.randomUUID().toString());
        btn.setKlass(v.getClass());
        btn.setIconSclass(v.getIcon() + " fa-lg");
        String color = v.getColor();
        String classes = "";
        if (v.getLabel() != null && !v.getLabel().isEmpty() && v.showLabel()) {
            btn.setLabel(v.getLabel());
            classes = "zk-btn-toolbar-text";
        } else
            classes = "zk-btn-toolbar";

        btn.setSclass(" " + classes + "  ");
        Popup popup = new Popup();
        popup.appendChild(new Label(v.getLabel()));
        btn.setPopup(popup);

        btn.setStyle(" background:" + (color == null || color.isEmpty() ? "#000" : color) + ";");
        CrudActionEvent event = new CrudActionEvent();
    //    event.setTabpanel(parent);
        event.setCrudViewParent(parent);
        btn.addEventListener(Events.ON_CLICK, e -> onClickListener(v, crudTable, event));
        String tooltipText = v.getTooltipText();
        btn.setTooltiptext(tooltipText != null ? tooltipText : "");
        appendChild(btn);
        return btn;
    }

    private void onClickListener(Action v, CrudDisplayTable crudTable, CrudActionEvent event) throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        Object selectedValue = crudTable.getSelectedValue();
        if (v.getFormState() == FormStates.CREATE)
            event.setValue(klass.getConstructor().newInstance());
        else {
            event.setValue(selectedValue);
        }
        event.setFormstate(v.getFormState());
        v.actionPerform(event);
    }


    void onlyEnable(List<String> actions) {
        this.actions.stream().forEach(e -> {
            if (!e.getId().isEmpty())
                e.setVisible(actions.contains(e.getId().split("-")[1]));
        });
    }

    void enableAction(Class<? extends Action> action, boolean enable) {
        if (ZKUtil.isMobile()) {
            actions.stream().map(e -> (ActionMobileButton) e).filter(e -> e.getKlass() == action).forEach(e -> e.setVisible(enable));
        } else {
            actions.stream().map(e -> (ActionButton) e).filter(e -> e.getKlass() == action).forEach(e -> e.setVisible(enable));
        }
    }


}
