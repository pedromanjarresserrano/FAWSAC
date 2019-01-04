package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.view.api.ContentView;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.*;
import org.zkoss.zul.impl.LabelImageElement;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component
@Scope("session")
public class ContentViewImpl implements ContentView {

    private Tabbox tab;
    @Autowired
    private FHSessionUtil fhSessionUtil;
    @Autowired
    private List<MenuProvider> menuProviders;

    @PostConstruct
    private void init() {
        tab = new Tabbox();
        Tabs tabs = new Tabs();
        tabs.setParent(tab);
        Tabpanels tabpanels = new Tabpanels();
        tabpanels.setHeight("99%");
        tab.setHeight("99%");
        tabpanels.setParent(tab);
        tab.appendChild(tabs);
        tab.appendChild(tabpanels);
        if (ZKUtil.isMobile()) tabs.setStyle("overflow-x: auto !important;");
    }

    @Override
    public Component getView() {
        if (tab.getPage() != null)
            init();
        return tab;
    }

    @Override
    public void addContent(MenuProvider provider) {
        String id = provider.getClass().getSimpleName();
        String label = provider.getLabel();
        Tabs tabs = tab.getTabs();
        List<Component> list = new ArrayList<>();
        if (tabs != null) {
            list = tabs.getChildren();
        }
        Optional<Component> existtab = getTab(id);
        try {
            if (existtab.isPresent()) {
                if (tab != null)
                    tab.setSelectedTab((Tab) existtab.get());
            } else {
                Tab tab = new Tab(label);
                tab.setId(id);
                list.add(tab);
                tab.setClosable(true);
                this.tab.getTabs().appendChild(tab);
                Component view = provider.getView();
                loadView(id, label, tab, view);
                tab.setIconSclass(provider.getIcon());
                this.tab.setSelectedTab(tab);

            }
        } catch (Exception e) {
            e.printStackTrace();
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
        menuProviders.stream().filter(e -> e.getGroup().compareToIgnoreCase(id)==0).filter(e -> e.getLabel().compareToIgnoreCase(menu)==0).forEach(this::addContent);
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
        if (existtab.isPresent()) {
            tab.setSelectedTab((Tab) existtab.get());
        } else {
            Tab tab = new Tab(label);
            tab.setId(id);
            tab.setClosable(true);
            this.tab.getTabs().appendChild(tab);
            org.zkoss.zk.ui.Component view = component;
            loadView(id, label, tab, view);
            this.tab.setSelectedTab(tab);

        }
    }

    private Optional<Component> getTab(String id) {
        List<Component> list = tab.getTabs().getChildren();
        return list.stream()
                .filter(e -> e.getId().equalsIgnoreCase(id)).findFirst();
    }

    @Override
    public Component getTabView(String id) {
        Optional<Component> tab = getTab(id);
        return tab.isPresent() ? tab.get() : null;

    }

    private void loadView(String id, String label, Tab tab, Component view) {
        if (!(view instanceof Tabpanel)) {
            Tabpanel tabpanel = new Tabpanel();
            tab.setLabel(label);
            tabpanel.setStyle("overflow-y:auto;");
            tabpanel.setHeight("100%");
            tabpanel.setSclass("color-system");
            tabpanel.appendChild(view);
            this.tab.getTabpanels().appendChild(tabpanel);

        } else {
            CrudView crudView = (CrudView) view;
            if (fhSessionUtil.getCurrentUser().getTipo() != TipoUsuario.ROLE_ADMIN) {
                crudView.onlyEnable(fhSessionUtil.getCurrentUser().getUserprofiles().stream().flatMap(e -> e.getProvidersaccess().stream())
                        .filter(e -> e.getMenuprovider().equalsIgnoreCase(id))
                        .flatMap(e -> e.getActions().stream()).collect(Collectors.toList()));
            }
            crudView.setSclass("color-system");
            this.tab.getTabpanels().getChildren().add(crudView);
        }
    }

    @Override
    public void closeCurrent() {
        tab.getSelectedTab().close();
    }
}
