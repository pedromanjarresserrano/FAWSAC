package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.view.api.ContentView;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import com.gitlab.pedrioko.services.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;
import org.zkoss.zul.impl.LabelImageElement;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component("contentView")
@Scope("session")
public class ContentViewImpl implements ContentView {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentViewImpl.class);


    private List<Tabbox> tabbes = new ArrayList<>();
    @Autowired
    private FHSessionUtil fhSessionUtil;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private List<MenuProvider> menuProviders;

    @PostConstruct
    private void init() {
        Tabbox tab = new Tabbox();
        Tabs tabs = new Tabs();
        tabs.setParent(tab);
        Tabpanels tabpanels = new Tabpanels();
        tabpanels.setHeight("99%");
        tabpanels.setStyle("overflow-y:auto;");
        tab.setHeight("99%");
        tabpanels.setParent(tab);
        tab.appendChild(tabs);
        tab.appendChild(tabpanels);
        if (ZKUtil.isMobile()) tabs.setStyle("overflow-x: auto !important;");
        tabbes.add(tab);

    }

    @Override
    public Component getViewCurrent() {
        if (tabbes.isEmpty()) {
            init();
            return tabbes.get(0);
        } else {
            Desktop desktop = Executions.getCurrent().getDesktop();
            Optional<Tabbox> first = tabbes.stream().filter(e -> desktop.equals(e.getDesktop())).findFirst();
            return first.orElse(tabbes.get(0));
        }
    }

    @Override
    public Component getView() {
        List<Tabbox> collect = tabbes.stream().filter(e -> e.getDesktop() != null).filter(e -> !e.getDesktop().isAlive()).collect(Collectors.toList());
        tabbes.removeAll(collect);
        Tabbox tab = new Tabbox();
        Tabs tabs = new Tabs();
        tabs.setParent(tab);
        Tabpanels tabpanels = new Tabpanels();
        tabpanels.setHeight("99%");
        tab.setHeight("99%");
        tabpanels.setParent(tab);
        tab.appendChild(tabs);
        tab.appendChild(tabpanels);
        if (ZKUtil.isMobile()) tabs.setStyle("overflow-x: auto !important;");
        tabbes.add(tab);
        tab.addEventListener(Events.ON_AFTER_SIZE, e -> securityService.getProvider(fhSessionUtil.getCurrentUser())
                .stream()
                .filter(MenuProvider::isOpenByDefault)
                .filter(f -> !getTab(f.getClass().getSimpleName()).isPresent())
                .forEachOrdered(this::addContent));
        return tab;
    }

    @Override
    public void addContent(MenuProvider provider) {
        String id = provider.getClass().getSimpleName();
        String label = provider.getLabel();
        Tabbox tabview = ((Tabbox) getViewCurrent());
        Tabs tabs = tabview.getTabs();
        List<Component> list = new ArrayList<>();
        if (tabs != null) {
            list = tabs.getChildren();
        }
        Optional<Component> existtab = getTab(id);
        try {
            if (existtab.isPresent()) {
                if (tabview != null)
                    tabview.setSelectedTab((Tab) existtab.get());
            } else {
                Tab tab = new Tab(label);
                tab.setId(id);
                list.add(tab);
                tab.setClosable(true);
                tabview.getTabs().appendChild(tab);
                Executions.getCurrent().setAttribute("menuprovider", provider);
                loadView(id, label, tab, provider.getView());
                tab.setIconSclass(provider.getIcon());
                tabview.setSelectedTab(tab);

            }
        } catch (Exception e) {
            LOGGER.error("ERROR ON addContent", e);
        }

    }

    @Override
    public void changeLabel(String id, String newLabel) {
        Optional<Component> tab = getTab(id);
        if (tab.isPresent())
            ((LabelImageElement) tab.get()).setLabel(newLabel);
    }

    @Override
    public void loadView(String id, String menu) {
        menuProviders.stream().filter(e -> e.getGroup().compareToIgnoreCase(id) == 0).filter(e -> e.getLabel().compareToIgnoreCase(menu) == 0).forEach(this::addContent);
    }

    @Override
    public void changeIcon(String id, String icon) {
        Optional<Component> tab = getTab(id);
        if (tab.isPresent())
            ((Tab) tab.get()).setIconSclass(icon);
    }

    @Override
    public void addView(Component component, String id, String label) {
        Optional<Component> existtab = getTab(id);
        Tabbox tabview = ((Tabbox) getViewCurrent());
        if (existtab.isPresent()) {
            tabview.setSelectedTab((Tab) existtab.get());
        } else {
            Tab tab = new Tab(label);
            tab.setId(id);
            tab.setClosable(true);
            tabview.getTabs().appendChild(tab);
            loadView(id, label, tab, component);
            tabview.setSelectedTab(tab);

        }
    }

    private Optional<Component> getTab(String id) {
        List<Component> list = ((Tabbox) getViewCurrent()).getTabs().getChildren();
        return list.stream()
                .filter(e -> e.getId().equalsIgnoreCase(id)).findFirst();
    }

    @Override
    public Component getTabView(String id) {
        Optional<Component> tab = getTab(id);
        return tab.isPresent() ? tab.get() : null;

    }

    private void loadView(String id, String label, Tab tab, Component view) {
        Tabbox tabview = ((Tabbox) getViewCurrent());
        if (!(view instanceof Tabpanel)) {
            Tabpanel tabpanel = new Tabpanel();
            tab.setLabel(label);
            tabpanel.setHeight("100%");
            tabpanel.appendChild(view);
            tabview.getTabpanels().appendChild(tabpanel);

        } else {
            CrudView crudView = (CrudView) view;
            crudView.setSclass("color-system");
            tabview.getTabpanels().getChildren().add(crudView);
        }
    }

    @Override
    public void closeCurrent() {
        ((Tabbox) getViewCurrent()).getSelectedTab().close();
    }
}
