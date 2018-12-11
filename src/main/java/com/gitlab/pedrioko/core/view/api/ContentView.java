package com.gitlab.pedrioko.core.view.api;

import org.zkoss.zk.ui.Component;

public interface ContentView {

    Component getView();

    void addContent(MenuProvider component);

    void changeLabel(String id, String newLabel);

    void changeIcon(String id, String newLabel);

    void addView(Component component, String id, String label);

    Component getTabView(String id);

    void closeCurrent();

}
