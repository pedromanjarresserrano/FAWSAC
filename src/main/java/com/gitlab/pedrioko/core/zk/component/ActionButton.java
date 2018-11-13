package com.gitlab.pedrioko.core.zk.component;

import lombok.Data;
import org.zkoss.zul.Toolbarbutton;

public @Data
class ActionButton extends Toolbarbutton {
    private Class<?> klass;
}
