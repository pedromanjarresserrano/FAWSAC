package com.gitlab.pedrioko.core.view.api;

import org.zkoss.zk.ui.Component;

public interface ContentView {

    Component getViewCurrent();

    Component getView();

    void addContent(MenuProvider component);

    void changeLabel(String id, String newLabel);

    void loadView(String id, String menu);

    void changeIcon(String id, String newLabel);

    void addView(Component component, String id, String label);

    Component getTabView(String id);

    void closeCurrent();

}
