package com.gitlab.pedrioko.core.zk.component.model;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

public class PlayEvent extends Event {

    public PlayEvent(String name) {
        super(name);
    }

    public PlayEvent(String name, Component target) {
        super(name, target);
    }

    public PlayEvent(String name, Component target, Object data) {
        super(name, target, data);
    }
}