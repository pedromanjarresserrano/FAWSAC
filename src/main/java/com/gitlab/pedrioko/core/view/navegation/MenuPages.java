package com.gitlab.pedrioko.core.view.navegation;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Layout;

public @Data
class MenuPages extends Layout {
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuPages.class);

    public MenuPages() {
        setStyle("width:100%;");
        setZclass("empty");
        appendChild(Executions.createComponents("~./zul/nav/menu.zul", null, null));
    }



}
