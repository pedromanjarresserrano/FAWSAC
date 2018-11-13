package com.gitlab.pedrioko.core.view.api;

import org.zkoss.zk.ui.Component;

public interface ContentView {

    Component getView();

    void addContent(MenuProvider component);

    void addView(Component component, String id, String label);

    void closeCurrent();

}
