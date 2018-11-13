package com.gitlab.pedrioko.core.view.navegation;

import com.gitlab.pedrioko.core.view.api.ContentView;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;

public class ContentTab extends Div {

    public ContentTab() {
        super();
        Component view = ApplicationContextUtils.getBean(ContentView.class).getView();
        appendChild(view);
    }
}
